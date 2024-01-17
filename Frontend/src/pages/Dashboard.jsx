import React, { useEffect, useState } from 'react';
import { BsCurrencyDollar } from 'react-icons/bs';
import { BsThreeDots } from "react-icons/bs";
import { GoPrimitiveDot } from 'react-icons/go'
import { Pracownicy, Pie, Button, LineChart, SparkLine } from '../components';
import { SparklineAreaData, ecomPieChartData } from '../data/dummy'
import { useStateContext } from '../contexts/ContextProvider';
import { Header } from '../components'
import { MdOutlineEmail } from 'react-icons/md'
import { VictoryPie } from 'victory';
import { BsEyeFill } from 'react-icons/bs'
import { Link } from 'react-router-dom';
import { MdOutlineSupervisorAccount } from 'react-icons/md';
import { IoSunnyOutline } from "react-icons/io5";
import { FiBarChart } from 'react-icons/fi';
import { BsHouse } from 'react-icons/bs';

const Dashboard = () => {
  const { currentColor } = useStateContext();
  const [annualSums, setAnnualSums] = useState({}); // Tutaj tworzymy stan za pomocą hooka useState

  useEffect(() => {
    // Funkcja do pobierania danych z API
    const fetchAnnualSums = async () => {
      try {
        const response = await fetch('http://localhost:8081/payments/annual-summary/2023');
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        setAnnualSums(data); // Zaktualizuj stan komponentu danymi z API
      } catch (error) {
        console.error("Błąd podczas pobierania danych:", error);
      }
    };

    fetchAnnualSums(); // Wywołaj funkcję przy montowaniu komponentu
  }, []); // Pusta tablica zależności, aby wykonać tylko raz

  const earningData = [
    {
      icon: <MdOutlineSupervisorAccount />,
      amount: annualSums.totalGuests || 0,
      percentage: '',
      title: 'Klienci',
      iconColor: '#03C9D7',
      iconBg: '#E5FAFB',
      pcColor: 'red-600',
      url: '/klienci',
    },
    {
      icon: <BsHouse />,
      amount: annualSums.totalHouses || 0,
      percentage: '',
      title: 'Domki',
      iconColor: 'rgb(255, 244, 229)',
      iconBg: 'rgb(254, 201, 15)',
      pcColor: 'green-600',
      url: '/domki',
    },
    {
      icon: <BsCurrencyDollar />,
      amount: annualSums.totalIncome || 0, 
      percentage: '',
      title: 'Przychód',
      iconColor: 'rgb(0, 194, 146)',
      iconBg: 'rgb(235, 250, 242)',
      pcColor: 'red-600',
      url: '/platnosci',
    },
    {
      icon: <IoSunnyOutline />,
      amount: annualSums.totalClimateFee || 0,
      percentage: '',
      title: 'Klimatyczne',
      iconColor: 'rgb(0, 194, 146)',
      iconBg: 'rgb(235, 250, 242)',
      pcColor: 'red-600',
      iconColor: 'rgb(228, 106, 118)',
      iconBg: 'rgb(255, 244, 229)',
      pcColor: 'green-600',
      url: '/rezerwacje',
    },
    {
      
      icon: <FiBarChart />,
      amount: annualSums.totalReservations || 0,
      percentage: '',
      title: 'Rezerwacje',
      iconColor: 'rgb(1 00, 100, 146)',
      iconBg: 'rgb(220, 220, 250)',
      pcColor: 'red-600',
      url: '/rezerwacje',
    },
  ];

  return (
      <div className='px-10 flex-col'>
        <div className='flex 
        w-full justify-center'>
          
          <div className='flex mt-5
          justify-center gap-8 items-center'>
            {earningData.map((item) => (
              <div
                key={item.title}
                className="
                flex bg-white items-start
                dark:text-gray-200
                dark:bg-secondary-dark-bg
                w-64 p-6 gap-5 rounded-xl"
              >
                <Link to={item.url}
                style={{color: item.iconColor,
                backgroundColor: item.iconBg}}
                className='text-2xl opacity-0.9
                rounded-full p-5 hover:drop-shadow-xl'>
                  {item.icon}
                </Link>
                <div className='w-full items-start'>
                  <p className='mt-3'>
                    <span className='text-lg
                    font-semibold'>
                      {item.amount}
                    </span>
                  </p>

                <p className='text-sm
                text-gray-400 mt-1'>
                  {item.title}
                </p>
                
                </div>
                <button type='button'
                  className='ml-auto'>
                    <BsThreeDots/>
                </button>
              </div>
            ))}
          </div>
        </div>
        <div className='bg-white
        dark:text-gray-200
        dark:bg-secondary-dark-bg
        p-4 mt-4 rounded-2xl w-full'>
          <div className='flex justify-between'>
            <div className='flex items-center gap-4'>
                <span className='text-lg text-zinc-800
                      font-semibold ml-7'>
                  Podatki:
                </span>
              
            </div>
          </div>

          <div className='flex gap-20'>
            <div className='border-r-1
            border-color m-7 pr-10'>
              <div>
                <p>
                  <span className='text-3xl
                  font-semibold'>
                    {annualSums.netIncome || 0}
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-green-400 ml-3
                  text-xs'>
                    69%
                  </span>
                </p>
                <p className='text-gray-500
                mt-1'>
                  Dochód
                </p>
              </div>
              <div className='mt-7'>
                <p>
                  <span className='text-3xl
                  font-semibold'>
                    {annualSums.vatTax || 0}
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-red-400 ml-3
                  text-xs'>
                    23%
                  </span>
                </p>
                <p className='text-gray-500
                mt-1'>
                  VAT
                </p>
              </div>
              <div className='mt-7'>
                <p>
                  <span className='text-3xl font-semibold'>
                    {annualSums.incomeTax || 0}
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-red-400 ml-3
                  text-xs'>
                    8%
                  </span>
                </p>
                <p className='text-gray-500 mt-1'>
                  Dochodowy
                </p>
              </div>
            </div>
            <div className='bg-balck'>
            <Pracownicy
            width='600px'
            height='420px'/>
            </div>
            <div>
              <div className='w-96 h-48 bg-light-pink p-7
              rounded-2xl flex ml-auto
              '>
                  <div>
                    <div className='flex items-center'>
                      <MdOutlineEmail size={25} className='mr-2 fill-zinc-800'/>
                      <h1 className='text-lg text-zinc-800
                      font-semibold'>
                        Email
                      </h1>
                    </div>
                    <h1 className='text-sm mt-1 text-zinc-700' fill={'#AE9DB5'}>
                      Nowe wiadomości
                    </h1>
                    <h1 className='mt-3 text-5xl text-zinc-800
                      font-semibold'>
                        58
                    </h1>
                    
                  </div>
                  <div className='relative flex items-center justify-center ml-auto'>
                        <text className='absolute font-semibold'>
                          20%
                        </text>
                        <VictoryPie
                        className='absolute'
                                    padAngle={0}
                                    labelComponent={<span/>}
                                    innerRadius={350}
                                    width={1000} height={1000}
                                    data={[{'key': "", 'y': 20}, {'key': "", 'y': (100-20)} ]}
                                    colorScale={["#FFFFFF", "#E8C3D0" ]}
                        />
                    </div>

                    <button type='button'
                    className='ml-auto h-4'
                    >
                      <BsThreeDots/>
                  </button>
              </div>
              <div className='w-96 h-48 bg-light-blue p-7 mt-6
              rounded-2xl flex ml-auto
              '>
                  <div>
                    <div className='flex items-center'>
                      <BsEyeFill size={25} className='mr-2 fill-zinc-800'/>
                      <h1 className='text-lg text-zinc-800
                      font-semibold'>
                        Zainteresowanie
                      </h1>
                    </div>
                    <h1 className='text-sm mt-1 text-zinc-700' fill={'#AE9DB5'}>
                      Użytkownicy
                    </h1>
                    <h1 className='mt-3 text-5xl text-zinc-800
                      font-semibold'>
                        5498
                    </h1>
                    
                  </div>
                  <div className='relative flex items-center justify-center ml-auto'>
                        <text className='absolute font-semibold'>
                          60%
                        </text>
                        <VictoryPie
                        className='absolute'
                                    padAngle={0}
                                    labelComponent={<span/>}
                                    innerRadius={350}
                                    width={1000} height={1000}
                                    data={[{'key': "", 'y': 40}, {'key': "", 'y': (100-40)} ]}
                                    colorScale={["#FFFFFF", "#D2D6E8" ]}
                        />
                    </div>

                    <button type='button'
                    className='ml-auto h-4'
                    >
                      <BsThreeDots/>
                  </button>
              </div>
              
              
            </div>
        </div>
        </div>


    </div>
  )
}

export default Dashboard