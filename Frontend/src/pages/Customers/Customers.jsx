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
import { customersGrid } from './CustomersGrid';
import LoadingSpinner from '../LoadingSpinner';

const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const selectionsettings = { type: 'None' };
  const toolbarOptions = ['Add', 'Edit', 'Delete', 'Search'];
  const editing = { allowDeleting: true, allowEditing: true, allowAdding: true, mode: 'Dialog', dialog: {} };

  const getTokenHeader = () => {
    const token = window.localStorage.getItem("token");
    return token ? { 'Authorization': `Bearer ${token}` } : {};
  };

  useEffect(() => {
    setLoading(true);
    fetch('http://localhost:8081/customers', { headers: getTokenHeader() })
      .then(response => {
        if (!response.ok) {
          throw new Error('Błąd sieciowy podczas pobierania danych');
        }
        return response.json();
      })
      .then(data => {
        setCustomers(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);
  
  const handleActionComplete = async (args) => {
    const customerData = args.data;
    const headers = {
      'Content-Type': 'application/json',
      ...getTokenHeader()
    };
  
    if (args.requestType === 'save') {
      let url = 'http://localhost:8081/customers';
      let method = 'POST';
      if (customerData.id) {
        url = `http://localhost:8081/customers/${customerData.id}`;
        method = 'PUT';
      }
  
      try {
        const response = await fetch(url, {
          method,
          headers,
          body: JSON.stringify(customerData),
        });
  
        if (response.ok) {
          // Po pomyślnym dodaniu lub edycji, pobierz pełną listę klientów
          const updatedCustomers = await fetch('http://localhost:8081/customers', { headers: getTokenHeader() })
            .then(response => response.json());
          setCustomers(updatedCustomers);
        } else {
          throw new Error('Błąd sieciowy podczas operacji');
        }
      } catch (err) {
        setError(err.message);
      }
    } else if (args.requestType === 'delete') {
      const deletedCustomer = args.data[0];
      const customerId = deletedCustomer.id;
  
      try {
        const response = await fetch(`http://localhost:8081/customers/${customerId}`, {
          method: 'DELETE',
          headers: getTokenHeader(),
        });
  
        if (response.ok) {
          setCustomers(customers.filter(customer => customer.id !== customerId));
        } else {
          throw new Error('Błąd sieciowy podczas usuwania klienta');
        }
      } catch (err) {
        setError(err.message);
      }
    }
  };   

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <div>Wystąpił błąd: {error}</div>;
  }

  const handleActionBegin = (args) => {
    if (args.requestType === 'beginEdit' || args.requestType === 'add') {
        setTimeout(() => {
            if (document.querySelector('.e-dlg-header')) {
                document.querySelector('.e-dlg-header').textContent = 'Szczegóły';
            }
        }, 0);
    }
};

  return (
      <GridComponent
        locale='pl'
        className="ml-8 mr-8 mt-8 bg-white"
        dataSource={customers}
        enableHover={false}
        allowPaging
        pageSettings={{ pageSize: 12, pageCount: 3}}
        selectionSettings={selectionsettings}
        toolbar={toolbarOptions}
        editSettings={editing}
        allowSorting
        actionBegin={handleActionBegin}
        actionComplete={handleActionComplete}
      >
        <ColumnsDirective>
          {customersGrid.map((item, index) => <ColumnDirective key={index} {...item} />)}
        </ColumnsDirective>
        <Inject services={[Page, Toolbar, Edit, Sort, Filter, Search]} />
      </GridComponent>
  );
};

export default Customers;
