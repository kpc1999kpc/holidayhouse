import React, { useEffect, useState } from 'react';
import { BsCurrencyDollar } from 'react-icons/bs';
import { BsThreeDots } from "react-icons/bs";
import { GoPrimitiveDot } from 'react-icons/go'
import { Pracownicy, Pie, Button, LineChart, SparkLine } from '../components';
import { SparklineAreaData, ecomPieChartData } from '../data/dummy'
import { useStateContext } from '../contexts/ContextProvider';
import { Header } from '../components'
import { MdOutlineEmail } from 'react-icons/md'
import { CiMoneyBill } from "react-icons/ci";
import { VictoryPie } from 'victory';
import { BsEyeFill } from 'react-icons/bs'
import { Link } from 'react-router-dom';
import { MdOutlineSupervisorAccount } from 'react-icons/md';
import { IoSunnyOutline } from "react-icons/io5";
import { FiBarChart } from 'react-icons/fi';
import { BsHouse } from 'react-icons/bs';
import axios from 'axios';

const Dashboard = () => {
  const { currentColor, activeMenu } = useStateContext();
  const [annualSums, setAnnualSums] = useState({}); // Tutaj tworzymy stan za pomocą hooka useState

  useEffect(() => {
    const fetchAnnualSums = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const response = await axios.get('http://localhost:8081/payments/annual-summary/2023', {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          setAnnualSums(response.data);
        } catch (error) {
          console.error("Błąd podczas pobierania danych:", error);
        }
      }
    };

    fetchAnnualSums();
  }, []);

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
      amount: annualSums.annualIncome || 0, 
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
      <div className='flex-col pt-3'>
        <div
          style={{ 
            maxWidth: activeMenu ? '1373px' : 'none', 
            width: '100%'
          }} 
          className='flex justify-center'
        >
          <div className='flex mt-5
          justify-center gap-8 items-center'>
            {earningData.map((item) => (
              <div
                key={item.title}
                style={{ 
                  width: activeMenu ? '233px' : '290px',
                }}
                className="
                flex bg-white items-start
                dark:text-gray-200
                dark:bg-secondary-dark-bg
                p-6 gap-5 rounded-xl"
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
        <div className='flex justify-center items-center'>
          <div
          style={{ width: '1293px', }}
          className='bg-white
          dark:text-gray-200
          dark:bg-secondary-dark-bg
          mt-8 pt-10 rounded-2xl mx-10 pb-7'>
            <div className='flex gap-20'>
              <div className='ml-7 pr-10 w-20'>
                <div>
                <div className='pb-9'>
                  <span className='text-lg text-zinc-600
                        font-semibold ml-7'>
                    Podatki:
                  </span>
                  </div>
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
              <div className='border-l-1 border-color'>
              <Pracownicy
                width='600px'
                height='420px'
                data={annualSums.monthlyEarnings}
              />
              </div>
              <div>
                <div className='h-48 bg-light-pink p-7
                rounded-2xl flex ml-auto'
                style={{ 
                  width: '385px',
                }}>
                    <div>
                      <div className='flex items-center'>
                        <CiMoneyBill size={30} className='mr-2 fill-zinc-800'/>
                        <h1 className='text-lg text-zinc-800
                        font-semibold'>
                          Obrót Firmy
                        </h1>
                      </div>
                        <h1 className='text-sm mt-1 text-zinc-700' fill={'#AE9DB5'}>
                          Przychód całkowity
                        </h1>
                        <h1 className='mt-3 text-5xl text-zinc-800
                          font-semibold'>
                            {annualSums.totalIncomeAllYears || 0}
                        </h1>
                    </div>
                    <div className='relative flex items-center justify-center ml-auto'
                    >
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
                <div className='h-48 bg-light-blue p-7 mt-6
                rounded-2xl flex ml-auto'
                style={{ 
                  width: '385px',
                }}>
                    <div>
                      <div className='flex items-center'>
                        <BsEyeFill size={25} className='mr-2 fill-zinc-800'/>
                        <h1 className='text-lg text-zinc-800
                        font-semibold'>
                          Zainteresowanie
                        </h1>
                      </div>
                      <h1 className='text-sm mt-1 text-zinc-700' fill={'#AE9DB5'}>
                        Całkowita liczba klientów
                      </h1>
                      <h1 className='mt-3 text-5xl text-zinc-800
                        font-semibold'>
                          {annualSums.totalGuestsAllYears || 0}
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


    </div>
  )
}

export default Dashboard