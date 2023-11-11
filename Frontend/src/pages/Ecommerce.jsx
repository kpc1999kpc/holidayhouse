import React from 'react';
import { BsCurrencyDollar } from 'react-icons/bs';
import { BsThreeDots } from "react-icons/bs";
import { GoPrimitiveDot } from 'react-icons/go'
import { Pracownicy, Pie, Button, LineChart, SparkLine } from '../components';
import { earningData, SparklineAreaData,
ecomPieChartData } from '../data/dummy'
import { useStateContext } from '../contexts/ContextProvider';
import { Header } from '../components'
import { MdOutlineEmail } from 'react-icons/md'
import { VictoryPie } from 'victory';
import { BsEyeFill } from 'react-icons/bs'
import { Link } from 'react-router-dom';

const Ecommerce = () => {
  const { currentColor } = useStateContext()
  return (
      <div className='px-10'>
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
            <div className='flex items-center
            gap-4'>

                <span className='text-lg text-zinc-800
                      font-semibold ml-7'>
                  Statystyki miesiąca:
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
                    $700.00
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-green-400 ml-3
                  text-xs'>
                    12%
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
                    $5800
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-green-400 ml-3
                  text-xs'>
                    7%
                  </span>
                </p>
                <p className='text-gray-500
                mt-1'>
                  Przychód
                </p>
              </div>
              <div className='mt-7'>
                <p>
                  <span className='text-3xl
                  font-semibold'>
                    $500
                  </span>
                  <span className='p-1.5
                  hover:drop-shadow-xl
                  cursor-pointer
                  rounded-full
                  text-white
                  bg-red-400 ml-3
                  text-xs'>
                    -3%
                  </span>
                </p>
                <p className='text-gray-500 mt-1'>
                  Koszty
                </p>
              </div>
              <div className='mt-10'>
                <Button
                  color='white'
                  bgColor={ currentColor }
                  text='Pobierz Raport'
                  borderRadius='10px'
                />
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

export default Ecommerce