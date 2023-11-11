import React, { useEffect } from 'react'
import { HiMenu } from 'react-icons/hi'
import { FiShoppingCart } from 'react-icons/fi'
import { BsChatLeft } from 'react-icons/bs'
import { RiNotification3Line } from 'react-icons/ri'
import { Tooltip, TooltipComponent } from '@syncfusion/ej2-react-popups'
import {Cart, Chat, Notification} from '.'
import { useStateContext } from '../contexts/ContextProvider'
import { Link, NavLink } from 'react-router-dom';
import { HiHome } from 'react-icons/hi';

const NavButton = ({title, customFunc, icon, color, dotColor}) => (
  <TooltipComponent content={title}
  position="BottomCenter">
    <button type='button'
    onClick={customFunc}
    style={{color}}
    className='relative text-xl rounded-full p-3
    hover:bg-light-gray'
    >
      <span style={{ background: dotColor}}
      className='absolute inline-flex
      rounded-full h-2 w-2 right-2 top-2'
      />
      {icon}
    </button>
  </TooltipComponent>
)

const Navbar = () => {
  const { activeMenu, setActiveMenu, isClicked,
    setIsClicked, handleClick, screenSize,
    setScreenSize, currentColor } = useStateContext()
  
  useEffect(() => {
    const handleResize = () => setScreenSize
    (window.innerWidth)

    window.addEventListener('resize',
    handleResize)

    handleResize()

    return () => window.removeEventListener
    ('resize', handleResize)
  }, [])

  useEffect(() => {
    if(screenSize <= 900) {
      setActiveMenu(false)
    } else {
      setActiveMenu(true)
    }
  }, [screenSize])

  return (
    <div className='flex justify-between sticky pl-1 top-0 w-full bg-white z-50 h-14'>
    <div className='flex items-center'>
        <NavButton
            title='Menu'
            customFunc={() => setActiveMenu((prevActiveMenu) => !prevActiveMenu)}
            color='#A9A9A9'
            icon={<HiMenu className="w-6 h-6"/>}
        />
        <Link
            to="/"
            className='items-center gap-3 flex text-xl font-extrabold tracking-tight dark:text-white text-slate-900'
        > 
            <HiHome color={ currentColor }/>
            <span style={{ color: currentColor }}>HolidayHouse</span>
        </Link>
    </div>
    <div className='flex'>
      <NavButton
        title='Cart'
        customFunc={() => handleClick('cart')}
        color={ currentColor }
        icon={<FiShoppingCart />} />
      <NavButton
        title='Chat'
        dotColor='#4ADE80'
        customFunc={() => handleClick('chat')}
        color={ currentColor }
        icon={<BsChatLeft />} />
      <NavButton
        title='Notification'
        customFunc={() => handleClick('notification')}
        color={ currentColor }
        icon={<RiNotification3Line />} />
        {isClicked.cart && <Cart />}
        {isClicked.chat && <Chat />}
        {isClicked.notification && <Notification />}
      </div>
    </div>
  )
}

export default Navbar