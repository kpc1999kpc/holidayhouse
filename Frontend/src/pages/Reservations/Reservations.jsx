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
import { DateRangePickerComponent } from '@syncfusion/ej2-react-calendars';
import { DateRangePicker } from 'rsuite';
import 'rsuite/dist/rsuite.css';


const customerList = [
  { id: 1, name: 'Jan Kowalski' },
  { id: 2, name: 'Anna Nowak' },
  // inne osoby...
];

// Komponent szablonu dla komentarzy
const CommentTemplate = (props) => (
  <div className="flex items-center justify-center h-full">
    <div className="flex items-center min-w-[50px] gap-2">
      <p>{props.comment || 'Brak'}</p>
    </div>
  </div>
);

// Parametry dla edytora daty
const dateParams = {
  params: {
    format: 'dd.MM.yyyy'
  }
};

// Konfiguracja kolumn dla tabeli rezerwacji
const reservationsGridColumns = [
  {
    field: 'customerFullName',
    headerText: 'Klient',
    width: '150',
    textAlign: 'Left',
    editType: 'dropdownedit',
    edit: {
      params: {
        create: () => {
          return new window.ej.dropdowns.DropDownList({
            dataSource: customerList,
            fields: { value: 'name' },
            allowFiltering: true,
            filtering: function (e) {
              let query = new window.ej.data.Query();
              query = (e.text !== '') ? query.where('name', 'contains', e.text, true) : query;
              e.updateData(customerList, query);
            }
          });
        }
      }
    }
  },
    {
      field: 'houseName',
      headerText: 'Domek',
      width: '150',
      textAlign: 'Center'
    },
    {
      field: 'guestsNumber',
      headerText: 'Ilość osób',
      width: '120',
      textAlign: 'Center',
    },     
    {
    field: 'dateRange',
    headerText: 'Pobyt',
    width: '300',
    textAlign: 'Center',
    template: ({ dateRange }) => {
      const format = date => new Date(date).toLocaleDateString('pl-PL');
      return (
        <span>{format(dateRange.checkIn)} - {format(dateRange.checkOut)}</span>
      );
    },
      editTemplate: () => (
        <>
        <p style={{
          fontSize: '0.75rem',
          fontWeight: '50',
          color: '#858585',
          margin: '0.5rem 0'
        }}>
          Pobyt
        </p>
        <DateRangePicker
        format="dd.MM.yyyy"
        character=" - "
        placement="top"
        placeholder="ㅤ"
        appearance="subtle"
        showOneCalendar
        size="sd"
        block
        ranges={[]}/>
        </>
      )
    },
    {
      field: 'comment',
      headerText: 'Komentarz',
      width: '160',
      textAlign: 'Center',
      template: CommentTemplate
    }
];

// Komponent rezerwacji
const Reservations = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const selectionsettings = { type: 'None' };
  const toolbarOptions = ['Add', 'Edit', 'Delete', 'Search'];
  const editing = { allowDeleting: true, allowEditing: true, allowAdding: true, mode: 'Dialog', dialog: {} };
  
  // Hook useEffect do pobierania danych rezerwacji
  useEffect(() => {
    fetch('http://localhost:8081/reservations')
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
  }, []);

  // Obsługa zakończenia akcji w komponencie tabeli
  const handleActionComplete = async (args) => {
    const reservationData = args.data;
    if (args.requestType === 'save') {
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
  };

  // Obsługa rozpoczęcia akcji
  const handleActionBegin = (args) => {
    if (args.requestType === 'beginEdit' || args.requestType === 'add') {
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
        {reservationsGridColumns.map((item, index) => (
          <ColumnDirective key={index} {...item} />
        ))}
      </ColumnsDirective>
      <Inject services={[Page, Toolbar, Edit, Sort, Filter, Search]} />
    </GridComponent>
  );
};

export default Reservations;