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
import LoadingSpinner from '../LoadingSpinner';
import 'rsuite/dist/rsuite.css';
import { DataManager, Query } from '@syncfusion/ej2-data';

const Reservations = () => {
  const [reservations, setReservations] = useState([]);
  const [customerData, setCustomerData] = useState([]);
  const [housesData, setHousesData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const reservationsGridColumns = [
    {
      field: 'customerFullName',
      headerText: 'Klient',
      width: '150',
      textAlign: 'Center',
      editType: 'dropdownedit',
      edit: {
        params: {
          actionComplete: () => false,
          allowFiltering: true,
          filterType: "contains",
          dataSource: new DataManager(customerData),
          fields: { value: 'name' },
          query: new Query()
        }
      }
    },
    {
      field: 'houseName',
      headerText: 'Domek',
      width: '100',
      textAlign: 'Center',
      editType: 'dropdownedit',
      edit: {
        params: {
          actionComplete: () => false,
          allowFiltering: false,
          dataSource: new DataManager(housesData),
          query: new Query()
        }
      }
    },
    {
      field: 'guests_number',
      headerText: 'Ilość osób',
      width: '100',
      textAlign: 'Center',
      editType: 'numericedit',
      edit: {
        params: {
          decimals: 0,
          format: "N",
        }
      }
    },
    {
      field: 'check_in',
      headerText: 'Zameldowanie',
      width: '100',
      textAlign: 'Center',
      editType: 'datepickeredit',
      type: 'date',
      format: 'dd.MM.yyyy',
      edit: {
        params: {
          format: 'dd.MM.yyyy'
        }
      }
    },
    {
      field: 'check_out',
      headerText: 'Wymeldowanie',
      width: '100',
      textAlign: 'Center',
      editType: 'datepickeredit',
      type: 'date',
      format: 'dd.MM.yyyy',
      edit: {
        params: {
          format: 'dd.MM.yyyy'
        }
      }
    },
    {
      field: 'nights',
      headerText: 'Doby',
      width: '70',
      textAlign: 'Center'
    },
    {
      field: 'comment',
      headerText: 'Komentarz',
      width: '100',
      textAlign: 'Center'
    }
  ];

  const [columns, setColumns] = useState(reservationsGridColumns);
  const selectionsettings = { type: 'None' };
  const toolbarOptions = ['Add', 'Edit', 'Delete', 'Search'];
  const editing = { allowDeleting: true, allowEditing: true, allowAdding: true, mode: 'Dialog', dialog: {} };
  
  // Hook useEffect do pobierania danych rezerwacji
  useEffect(() => {
    const token = localStorage.getItem('token');
    const headers = token ? { Authorization: `Bearer ${token}` } : {};

    // Pobieranie danych rezerwacji
    fetch('http://localhost:8081/reservations', { headers })
      .then(response => {
        if (!response.ok) {
          throw new Error('Błąd sieciowy podczas pobierania danych');
        }
        return response.json();
      })
      .then(data => {
        setReservations(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });

    // Pobieranie danych domków i klientów
    Promise.all([
      fetch('http://localhost:8081/houses/active', { headers }).then(response => response.json()),
      fetch('http://localhost:8081/customers/fullnames', { headers }).then(response => response.json())
    ])
      .then(([housesData, customerData]) => {
        setHousesData(housesData);
        setCustomerData(customerData);

        // Aktualizacja kolumn
        const updatedColumns = columns.map(column => {
          if (column.field === 'houseName') {
            return {
              ...column,
              edit: {
                ...column.edit,
                params: {
                  ...column.edit.params,
                  dataSource: new DataManager(housesData),
                  fields: { value: 'name' }
                }
              }
            };
          } else if (column.field === 'customerFullName') {
            return {
              ...column,
              edit: {
                ...column.edit,
                params: {
                  ...column.edit.params,
                  dataSource: new DataManager(customerData),
                  fields: { value: 'name' }
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
  const handleActionComplete = async (args) => {
    const reservationData = args.data;
    const token = localStorage.getItem('token'); // Pobierz token z localStorage
    const authHeaders = token ? { Authorization: `Bearer ${token}` } : {};

    if (args.requestType === 'save') {
      if (reservationData.check_in instanceof Date) {
        // Dodaj czas lokalny do daty, aby uniknąć przesunięcia UTC
        const offset = reservationData.check_in.getTimezoneOffset();
        const checkIn = new Date(reservationData.check_in.getTime() - offset * 60 * 1000);
        reservationData.check_in = checkIn.toISOString().split('T')[0];
      }
      if (reservationData.check_out instanceof Date) {
        // Dodaj czas lokalny do daty, aby uniknąć przesunięcia UTC
        const offset = reservationData.check_out.getTimezoneOffset();
        const checkOut = new Date(reservationData.check_out.getTime() - offset * 60 * 1000);
        reservationData.check_out = checkOut.toISOString().split('T')[0];
      }
      console.log('Formatted reservation data being sent to server:', reservationData);
      let url = 'http://localhost:8081/reservations';
      let method = 'POST';
      if (reservationData.id) {
        url = `http://localhost:8081/reservations/${reservationData.id}`;
        method = 'PUT';
      }

      try {
        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json',
            ...authHeaders
          },
          body: JSON.stringify(reservationData),
        });

        if (response.ok) {
          // Po pomyślnym dodaniu lub edycji, pobierz pełną listę domków
          fetch('http://localhost:8081/reservations')
            .then(response => response.json())
            .then(data => setReservations(data));
        } else {
          throw new Error('Błąd sieciowy podczas operacji');
        }
      } catch (err) {
        setError(err.message);
      }
    } else if (args.requestType === 'delete') {
      const deletedReservation = args.data[0];
      const reservationId = deletedReservation.id;

      try {
        const response = await fetch(`http://localhost:8081/reservations/${reservationId}`, {
          method: 'DELETE',
          headers: authHeaders
        });

        if (response.ok) {
          setReservations(reservations.filter(reservation => reservation.id !== reservationId));
        } else {
          throw new Error('Błąd sieciowy podczas usuwania domku');
        }
      } catch (err) {
        setError(err.message);
      }
    }

    if (args.requestType === 'save' || args.requestType === 'cancel') {
      const newColumns = columns.map(col => {
        if (col.field === 'nights') {
          return { ...col, visible: true };
        }
        return col;
      });
      setColumns(newColumns);
    }
  };

  // Obsługa rozpoczęcia akcji
  const handleActionBegin = (args) => {
    if (args.requestType === 'beginEdit' || args.requestType === 'add') {
      

      const newColumns = columns.map(col => {
        if (col.field === 'nights') {
          return { ...col, visible: false };
        }
        return col;
      });
      setColumns(newColumns);
  
      setTimeout(() => {
        if (document.querySelector('.e-dlg-header')) {
          document.querySelector('.e-dlg-header').textContent = 'Szczegóły';
        }
      }, 0);
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
      dataSource={reservations}
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

export default Reservations;