import React from 'react';
import { GridComponent, ColumnsDirective, ColumnDirective, Page, Search, Sort, Toolbar, Edit, Inject } from '@syncfusion/ej2-react-grids';
import { DateRangePickerComponent } from '@syncfusion/ej2-react-calendars';
import { employeesData, employeesGrid } from '../data/dummy';
import { Header } from '../components';

const DateRangePickerEditor = (props) => {
  return (
    <DateRangePickerComponent id={props.column.field} type="text"
      format='dd.MM.yyyy' value={props.value}
      change={(e) => props.onChange(e.value)}
    />
  );
};

const Employees = () => {
  const editSettings = {
    allowEditing: true,
    allowAdding: true,
    allowDeleting: true,
    mode: 'Dialog'
  };

  const toolbarOptions = ['Search', 'Add', 'Edit', 'Delete', 'Update', 'Cancel'];

  return (
    <div className='m-2 md:m-10 mt-24 p-2 md:p-10 bg-white rounded-3xl'>
      <Header category='Page' title='Pracownicy' />
      <GridComponent
        dataSource={employeesData}
        allowPaging
        allowSorting
        toolbar={toolbarOptions}
        editSettings={editSettings}
        width="auto"
      >
        <ColumnsDirective>
          {/* ... inne kolumny ... */}
          <ColumnDirective field='dateRange' headerText='Zakres dat' editType='customedit' edit={DateRangePickerEditor} />
          {employeesGrid.map((item, index) => (
            <ColumnDirective key={index} {...item} />
          ))}
        </ColumnsDirective>
        <Inject services={[Page, Search, Sort, Toolbar, Edit]} />
      </GridComponent>
    </div>
  );
};

export default Employees;
