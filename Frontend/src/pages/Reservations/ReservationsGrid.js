const CommentTemplate = (props) => (
  <div className="flex items-center justify-center h-full">
    <div className="flex items-center min-w-[50px] gap-2">
      <p>{props.comment || 'Brak'}</p>
    </div>
  </div>
);

export const dateParams = {
  params: {
    format: 'dd.MM.yyyy'
  }
};

export const reservationsGrid = [
    {
      field: 'customerFullName',
      headerText: 'Klient',
      width: '150',
      textAlign: 'Left'
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
      field: 'checkIn',
      headerText: 'Data Zameldowania',
      width: '80',
      textAlign: 'Center',
      editType: 'datepickeredit',
      format: 'dd.MM.yyyy',
      edit: dateParams  // Użyj dateParams tutaj
    },
    {
      field: 'checkOut',
      headerText: 'Data Wymeldowania',
      width: '80',
      textAlign: 'Center',
      editType: 'datepickeredit',
      format: 'dd.MM.yyyy',
      edit: dateParams  // I tutaj
    },
    {
      field: 'comment',
      headerText: 'Komentarz',
      width: '160',
      textAlign: 'Center',
      template: CommentTemplate
    }
];
