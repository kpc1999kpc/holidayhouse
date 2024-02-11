import React, { useEffect, useState } from 'react';
import { ScheduleComponent, ViewsDirective, ViewDirective, ResourcesDirective, ResourceDirective, TimelineViews, TimelineMonth, Inject } from '@syncfusion/ej2-react-schedule';

const Calendar = () => {
  const [houses, setHouses] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const getTokenHeader = () => {
    const token = window.localStorage.getItem("token");
    return token ? { 'Authorization': `Bearer ${token}` } : {};
  };

  useEffect(() => {
    const fetchOptions = {
      headers: {
        'Content-Type': 'application/json',
        ...getTokenHeader(),
      }
    };

    const fetchHouses = fetch('http://localhost:8081/houses', fetchOptions).then(res => res.json());
    const fetchReservations = fetch('http://localhost:8081/reservations', fetchOptions).then(res => res.json());

    Promise.all([fetchHouses, fetchReservations])
      .then(([housesData, reservationsData]) => {
        setHouses(housesData);
        setReservations(reservationsData);
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);
  
  const colors = [
    '#EA7A57', '#7FA900', '#5978EE', '#FEC200', '#DF5286',
    '#00BDAE', '#865FCF', '#1AAA55', '#E57373', '#41A5F5'
  ];
  
  // Generowanie danych zasobów dla domków od 1 do 10
  const resourceData = loading ? [] : houses.map((house, index) => ({
    Id: house.id,
    Text: house.name,
    Color: colors[index % colors.length] // Cykliczne przypisanie kolorów
  }));
  

  const getPeopleText = (count) => {
    if (count === 1) return "1 osoba";
    else if (count > 1 && count < 5) return `${count} osoby`;
    else return `${count} osób`;
  };

  const getNightsText = (count) => {
    if (count === 1) return "1 doba";
    else if (count > 1 && count < 5) return `${count} doby`;
    else return `${count} dób`;
  };

  // Mapowanie danych rezerwacji na format zdarzeń
  const mapReservationsToEvents = () => {
    return reservations.map(reservation => ({
      Id: reservation.id,
      Subject: `${reservation.customerFullName} (${getPeopleText(reservation.guests_number)}, ${getNightsText(reservation.nights)})`,
      StartTime: new Date(reservation.check_in),
      EndTime: new Date(reservation.check_out),
      IsAllDay: true,
      ResourceId: reservation.houseId
    }));
  };

  const eventSettings = { dataSource: mapReservationsToEvents() };

  
  return (
    <div>
      {loading ? <p>Ładowanie danych...</p> : (
      <ScheduleComponent
        className='ml-8 mt-8'
        height='100%'
        width='95%'
        selectedDate={new Date(2023, 7, 1)}
        eventSettings={eventSettings}
        group={{ resources: ['Resources'] }}
        rowAutoHeight={true}
      >
        <ResourcesDirective>
          {resourceData.map((resource) => (
            <ResourceDirective
              key={resource.Id}
              field='ResourceId'
              title='Resource'
              name='Resources'
              dataSource={resourceData}
              textField='Text'
              idField='Id'
              colorField='Color'
            />
          ))}
        </ResourcesDirective>
        <ViewsDirective>
          <ViewDirective option='TimelineMonth' isSelected={true} />
        </ViewsDirective>
        <Inject services={[TimelineViews, TimelineMonth]} />
      </ScheduleComponent>
      )}
    </div>
  );
};

export default Calendar;