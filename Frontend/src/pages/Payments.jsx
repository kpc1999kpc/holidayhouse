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

const Payments = () => {
  const [payments, setPayments] = useState([]);
  const [reservationData, setReservationData] = useState([]);
  const [selectedReservationPayments, setSelectedReservationPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Konfiguracja kolumn dla tabeli płatności
  const paymentsGridColumns = [
    {
      field: 'payment_date',
      headerText: 'Data płatności',
      width: '150',
      textAlign: 'Center',
      editType: 'datepickeredit',
      format: 'dd.MM.yyyy',
      type: 'date',
      edit: { params: { format: 'dd.MM.yyyy' } }
    },
    {
      field: 'amount',
      headerText: 'Kwota',
      width: '150',
      textAlign: 'Center',
      editType: 'numericedit'
    },
    {
      field: 'comment',
      headerText: 'Komentarz',
      width: '200',
      textAlign: 'Center',
      editType: 'string'
    },
    {
      field: 'reservationId',
      headerText: 'ID Rezerwacji',
      width: '150',
      textAlign: 'Center',
      editType: 'dropdownedit',
      edit: {
        params: {
          actionComplete: () => false,
          allowFiltering: true,
          filterType: 'contains',
          dataSource: new DataManager(reservationData),
          fields: { value: 'id' },
          query: new Query()
        }
      }
    }
    // Dodaj więcej kolumn zgodnie z potrzebą
  ];

  useEffect(() => {
    Promise.all([
      fetch('http://localhost:8081/payments').then(res => res.json()),
      fetch('http://localhost:8081/reservations').then(res => res.json()),
    ]).then(([paymentsData, reservationsData]) => {
      setPayments(paymentsData);
      setReservationData(reservationsData);
      setLoading(false);
    }).catch(error => {
      setError(error.message);
      setLoading(false);
    });
  }, []);

  const handleRowSelected = (args) => {
    const selectedData = args.data;
    const filteredPayments = payments.filter(
      payment => payment.reservationId === selectedData.id
    );
    setSelectedReservationPayments(filteredPayments);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <div>Wystąpił błąd: {error}</div>;
  }

  return (
    <div>
      <GridComponent
        dataSource={reservationData}
        selectionSettings={{ type: 'Single' }}
        rowSelected={handleRowSelected}
      >
        <ColumnsDirective>
          <ColumnDirective field='id' headerText='ID Rezerwacji' width='120' textAlign='Center' />
          <ColumnDirective field='customerFullName' headerText='Nazwa Klienta' width='150' textAlign='Center' />
          <ColumnDirective field='houseName' headerText='Domek' width='150' textAlign='Center' />
          <ColumnDirective field='check_in' headerText='Data Przyjazdu' width='130' textAlign='Center' format='dd.MM.yyyy' type='date' />
          <ColumnDirective field='check_out' headerText='Data Wyjazdu' width='130' textAlign='Center' format='dd.MM.yyyy' type='date' />
      </ColumnsDirective>
      <Inject services={[Page, Toolbar, Edit, Sort, Filter, Search]} />
      </GridComponent>

      <GridComponent dataSource={selectedReservationPayments}>
        <ColumnsDirective>
          {paymentsGridColumns.map((col, index) => (
            <ColumnDirective key={index} {...col} />
          ))}
        </ColumnsDirective>
        <Inject services={[Page, Sort, Filter]} />
      </GridComponent>
    </div>
  );
};

export default Payments;
