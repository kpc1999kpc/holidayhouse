const CenteredText = (props) => (
    <div className="flex items-center justify-center h-full">
      <div className="flex items-center min-w-[95px]">
        <p>{props.Text}</p>
      </div>
    </div>
  );
  
export const customersGrid = [
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
        headerText: 'Imię',
        width: '150',
        textAlign: 'Center',
        template: (data) => <CenteredText Text={data.name} />
    },
    {
        field: 'surname',
        headerText: 'Nazwisko',
        width: '150',
        textAlign: 'Center',
        template: (data) => <CenteredText Text={data.surname} />
    },
    {
        field: 'phone_number',
        headerText: 'Numer telefonu',
        width: '150',
        textAlign: 'Center',
        template: (data) => <CenteredText Text={data.phone_number} />
    },
    {
        field: 'comment',
        headerText: 'Komentarz',
        width: '150',
        textAlign: 'Center',
        template: (data) => <CenteredText Text={data.comment} />
    },
];