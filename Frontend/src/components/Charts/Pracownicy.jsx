import React from 'react';
import { ChartComponent, SeriesCollectionDirective, SeriesDirective, Inject, Legend, Category, StackingColumnSeries, Tooltip } from '@syncfusion/ej2-react-charts';

const Pracownicy = ({ width, height, data }) => {
  if (!data) {
    return <div>Loading...</div>; // lub jakikolwiek inny komunikat lub komponent ładowania
  }

  // Przekształcenie danych z formatu obiektu na tablicę
  const miesiace = ['STY', 'LUT', 'MAR', 'KWI', 'MAJ', 'CZE', 'LIP', 'SIE', 'WRZ', 'PAZ', 'LIS', 'GRU'];
  const stackedChartData = miesiace.map((miesiac, index) => {
    // Przyjmując, że klucze w danych to liczby od 1 do 12
    const yValue = parseFloat(data[index + 1].replace('.', '').replace(',', '.')) || 0;
    return { x: miesiac, y: yValue };
  });

  const stackedCustomSeries = [
    { 
      dataSource: stackedChartData,
      xName: 'x',
      yName: 'y',
      name: 'Dochód',
      type: 'StackingColumn',
      background: 'blue',
    },
  ];

  const stackedPrimaryXAxis = {
    majorGridLines: { width: 0 },
    minorGridLines: { width: 0 },
    majorTickLines: { width: 0 },
    minorTickLines: { width: 0 },
    interval: 1,
    lineStyle: { width: 0 },
    labelIntersectAction: 'Rotate45',
    valueType: 'Category',
  };

  const stackedPrimaryYAxis = {
    lineStyle: { width: 0 },
    minimum: 100,
    maximum: 100000,
    interval: 50000,
    majorTickLines: { width: 0 },
    majorGridLines: { width: 1 },
    minorGridLines: { width: 1 },
    minorTickLines: { width: 0 },
    labelFormat: '{value}',
  };

  return (
    <ChartComponent
      width={width}
      height={height}
      id="charts"
      primaryXAxis={stackedPrimaryXAxis}
      primaryYAxis={stackedPrimaryYAxis}
      chartArea={{ border: {width: 0 }}}
      tooltip={{ enable: true }}
      legendSettings={{ background: 'white'}}
    >
      <Inject services={[Legend, Category, StackingColumnSeries, Tooltip]}/>
      <SeriesCollectionDirective>
        {stackedCustomSeries.map((item, index) =>
          <SeriesDirective key={index} {...item} />
        )}
      </SeriesCollectionDirective>
    </ChartComponent>
  );
}

export default Pracownicy;
