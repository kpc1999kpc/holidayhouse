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
  DropDownList
} from '@syncfusion/ej2-react-grids';
import { housesGrid } from './HousesGrid';
import LoadingSpinner from '../LoadingSpinner';

const Houses = () => {
  const [houses, setHouses] = useState([]);
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
    fetch('http://localhost:8081/houses', { headers: getTokenHeader() })
      .then(response => {
        if (!response.ok) {
          throw new Error('Błąd sieciowy podczas pobierania danych');
        }
        return response.json();
      })
      .then(data => {
        setHouses(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const handleActionComplete = async (args) => {
    const houseData = args.data;
    const headers = {
      'Content-Type': 'application/json',
      ...getTokenHeader()
    };

    if (args.requestType === 'save') {
      let url = 'http://localhost:8081/houses';
      let method = 'POST';
      if (houseData.id) {
        url = `http://localhost:8081/houses/${houseData.id}`;
        method = 'PUT';
      }

      try {
        const response = await fetch(url, {
          method,
          headers,
          body: JSON.stringify(houseData),
        });

        if (response.ok) {
          // Po pomyślnym dodaniu lub edycji, pobierz pełną listę domków
          const updatedHouses = await fetch('http://localhost:8081/houses', { headers: getTokenHeader() })
            .then(response => response.json());
          setHouses(updatedHouses);
        } else {
          throw new Error('Błąd sieciowy podczas operacji');
        }
      } catch (err) {
        setError(err.message);
      }
    } else if (args.requestType === 'delete') {
      const deletedHouse = args.data[0];
      const houseId = deletedHouse.id;

      try {
        const response = await fetch(`http://localhost:8081/houses/${houseId}`, {
          method: 'DELETE',
          headers: getTokenHeader(),
        });

        if (response.ok) {
          setHouses(houses.filter(house => house.id !== houseId));
        } else {
          throw new Error('Błąd sieciowy podczas usuwania domku');
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
        dataSource={houses}
        enableHover={false}
        allowPaging
        pageSettings={{ pageSize: 15, pageCount: 3}}
        selectionSettings={selectionsettings}
        toolbar={toolbarOptions}
        editSettings={editing}
        allowSorting
        actionBegin={handleActionBegin}
        actionComplete={handleActionComplete}
      >
        <ColumnsDirective>
          {housesGrid.map((item, index) => <ColumnDirective key={index} {...item} />)}
        </ColumnsDirective>
        <Inject services={[Page, Toolbar, Edit, Sort, Filter, Search]} />
      </GridComponent>
  );
};

export default Houses;
