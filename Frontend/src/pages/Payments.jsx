import React, { useState, useEffect } from 'react';
import {
  GridComponent,
  ColumnsDirective,
  ColumnDirective,
  Page,
  Inject,
  Edit,
  Toolbar,
  Sort,
  Filter,
  Search,
} from '@syncfusion/ej2-react-grids';
import LoadingSpinner from './LoadingSpinner';
import 'rsuite/dist/rsuite.css';
import { DataManager, Query } from '@syncfusion/ej2-data';

// Komponent płatności
const Payments = () => {
  const [payments, setPayments] = useState([]);
  const [reservationData, setReservationData] = useState([]);
  
  const getTokenHeader = () => {
    const token = window.localStorage.getItem("token");
    return token ? { 'Authorization': `Bearer ${token}` } : {};
  };

  // Konfiguracja kolumn dla tabeli płatności
  const paymentsGridColumns = [
    {
      field: 'reservationDetails',
      headerText: 'Klient (ilość osób), Domek: Zameldowanie - Wymeldowanie (ilość dób)',
      width: '135',
      textAlign: 'Left',
      editType: 'dropdownedit',
      edit: {
        params: {
          actionComplete: () => false,
          allowFiltering: true,
          filterType: "contains",
          dataSource: new DataManager(reservationData),
          fields: { text: 'name' },
          query: new Query()
        }
      }
    },
    {
      field: 'amount',
      headerText: 'Kwota',
      width: '30',
      textAlign: 'Right',
      editType: 'numericedit',
      format: 'C2'
    },
    {
      field: 'dailyRate',
      headerText: 'Cena za dobę',
      width: '30',
      textAlign: 'Right',
      format: 'C2',
      editType: 'numericedit'
    },
    {
      field: 'climateFee',
      headerText: 'Opłata klimatyczna',
      width: '35',
      textAlign: 'Right',
      editType: 'numericedit',
      format: 'C2'
    }
  ];

  const [columns, setColumns] = useState(paymentsGridColumns);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const selectionsettings = { type: 'None' };
  const toolbarOptions = ['Add', 'Edit', 'Delete', 'Search'];
  const editing = { allowDeleting: true, allowEditing: true, allowAdding: true, mode: 'Dialog', dialog: {} };
  
  // Hook useEffect do pobierania danych płatności
  useEffect(() => {
    // Pobieranie danych płatności
    fetch('http://localhost:8081/payments', {
      headers: {
        'Content-Type': 'application/json',
        ...getTokenHeader(),
      }
      }).then(response => {
        if (!response.ok) {
          throw new Error('Błąd sieciowy podczas pobierania danych');
        }
        return response.json();
      })
      .then(data => {
        setPayments(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  
    // Pobieranie danych rezerwacji dla listy rozwijanej
    Promise.all([
      fetch('http://localhost:8081/reservations/details', {
        headers: {
          'Content-Type': 'application/json',
          ...getTokenHeader(),
        }
      }).then(response => response.json()),
    ])
    .then(([reservationData]) => {
      // Przetwarzanie i ustawianie danych domków
      setReservationData(reservationData);
  
      // Aktualizacja kolumn
      const updatedColumns = columns.map(column => {
        if (column.field === 'reservationDetails') {
          return {
            ...column,
            edit: {
              ...column.edit,
              params: {
                ...column.edit.params,
                dataSource: new DataManager(reservationData),
                fields: { text: 'name' },
              }
            }
          };
        } 
      
        return column;
      });
      setColumns(updatedColumns);
    })
    .catch(error => {
      console.error('Błąd podczas pobierania danych:', error);
      setError(error.message);
    });
  }, []);

  // Obsługa zakończenia akcji w komponencie tabeli
  // (zaktualizować logikę obsługi akcji zgodnie z wymaganiami dla płatności)
  const handleActionComplete = async (args) => {
    // Obsługa zapisu danych płatności

     
    if (args.requestType === 'save') {
      const paymentData = args.data;
      let url = 'http://localhost:8081/payments';
      let method = 'POST';

   
      console.log("Zapisane dane:", args.data);


      // Sprawdzenie, czy jest to edycja istniejącej płatności
      if (paymentData.id) {
          url = `http://localhost:8081/payments/${paymentData.id}`;
          method = 'PUT';
      }

      try {
          const response = await fetch(url, {
              method,
              headers: {
                  'Content-Type': 'application/json',
                  ...getTokenHeader(),
              },
              body: JSON.stringify(paymentData),
          });

          if (!response.ok) {
              throw new Error('Błąd sieciowy podczas operacji');
          }

          // Po pomyślnym dodaniu lub edycji, pobierz pełną listę płatności
          const updatedPayments = await fetch('http://localhost:8081/payments', {
            headers: {
              'Content-Type': 'application/json',
              ...getTokenHeader(), // Dodanie nagłówków z tokenem JWT
            }
          }).then(res => res.json());
          setPayments(updatedPayments);
      } catch (err) {
          setError(err.message);
      }
    } else if (args.requestType === 'delete') {
      const deletedPayment = args.data[0];
      const paymentId = deletedPayment.id;

      try {
          const response = await fetch(`http://localhost:8081/payments/${paymentId}`, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
                ...getTokenHeader(), // Dodanie nagłówków z tokenem JWT
              },
          });

          if (!response.ok) {
              throw new Error('Błąd sieciowy podczas usuwania płatności');
          }

          // Aktualizacja listy płatności po usunięciu
          setPayments(payments.filter(payment => payment.id !== paymentId));
      } catch (err) {
          setError(err.message);
      }


    }

    // Obsługa zakończenia akcji w komponencie tabeli
    if (args.requestType === 'save' || args.requestType === 'cancel') {
      const newColumns = columns.map(col => {
        if (col.field === 'climateFee' || col.field === 'dailyRate') {
          return { ...col, visible: true };
        }
        return col;
      });
      setColumns(newColumns);
    }

    if ((args.requestType === 'beginEdit' || args.requestType === 'add')) {
      args.dialog.width = 500;
    }
  };

  // Obsługa rozpoczęcia akcji
 const handleActionBegin = (args) => {
  if (args.requestType === 'beginEdit' || args.requestType === 'add') {
    const newColumns = columns.map(col => {
      if (col.field === 'climateFee' || col.field === 'dailyRate') {
        return { ...col, visible: false };
      }
      return col;
    });
    setColumns(newColumns);

  }
};

  // Renderowanie komponentu
  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <div>Wystąpił błąd: {error}</div>;
  }

  return (
    <GridComponent
      locale='pl'
      className="ml-8 mr-8 mt-8 bg-white"
      dataSource={payments}
      enableHover={false}
      allowPaging
      pageSettings={{ pageSize: 12, pageCount: 3 }}
      selectionSettings={selectionsettings}
      toolbar={toolbarOptions}
      editSettings={editing}
      allowSorting
      actionBegin={handleActionBegin}
      actionComplete={handleActionComplete}
    >
      <ColumnsDirective>
        {columns.map((item, index) => (
          <ColumnDirective key={index} {...item} />
        ))}
      </ColumnsDirective>
      <Inject services={[Page, Toolbar, Edit, Sort, Filter, Search]} />
    </GridComponent>
  );
};

export default Payments;