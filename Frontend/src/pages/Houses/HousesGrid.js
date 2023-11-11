import { Query } from '@syncfusion/ej2-data';

const getStatusColor = (status) => {
  switch(status) {
      case 'Wolny': return '#8BE78B';
      case 'Niedostępny': return '#E78B8B';
      case 'W naprawie': return '#E7D58B';
      case 'Recepcja': return '#8BBEE7';
      default: return '#CCCCCC';
  }
}

const HouseName = (props) => (
  <div className="flex items-center justify-center h-full">
    <div className="flex items-center min-w-[58px]">
      <p>{props.Name}</p>
    </div>
  </div>
);


const HouseStatus = (props) => (
  <div className="flex items-center justify-center h-full">
    <div className="flex items-center min-w-[93px] gap-2">
      <p style={{ background: props.StatusBg }} className="rounded-full h-3 w-3" />
      <p>{props.Status || 'Brak'}</p>
    </div>
  </div>
);

const HouseComment = (props) => (
  <div className="flex items-center justify-center h-full">
    <div className="flex items-center min-w-[200px] gap-2">
      <p>{props.Comment || 'Brak'}</p>
    </div>
  </div>
);

export const housesGrid = [
    {
        field: 'id',
        headerText: 'ID',
        width: '150',
        textAlign: 'Center',
        isPrimaryKey: true,
        edit: false,
        visible: false,
    },
    {
      field: 'name',
      headerText: 'Nazwa',
      width: '150',
      textAlign: 'Center',
      template: (data) => <HouseName Name={data.name} />
    },  
    {
      field: 'status',
      headerText: 'Status',
      textAlign: 'Center',
      template: (data) => <HouseStatus Status={data.status} StatusBg={getStatusColor(data.status)} />,
      editType: 'dropdownedit',
      width: '150',
      edit: {
        params: { 
          query: new Query(),
          dataSource: [
            { text: 'Wolny', value: 'Wolny' },
            { text: 'Niedostępny', value: 'Niedostępny' },
            { text: 'W naprawie', value: 'W naprawie' },
            { text: 'Recepcja', value: 'Recepcja' }
          ], 
          fields: { text: 'text', value: 'value' } 
        }
      }
    },     
    {
      field: 'comment',
      headerText: 'Komentarz',
      width: '150',
      textAlign: 'Center',
      template: (data) => <HouseComment Comment={data.comment} />
    },
  ];