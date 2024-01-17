import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { AiOutlineHome } from 'react-icons/ai';
import { MdOutlineCancel } from 'react-icons/md';
import { TooltipComponent } from '@syncfusion/ej2-react-popups';
import { links } from '../data/dummy';

import { useStateContext } from '../contexts/ContextProvider';
import {UserProfile } from '.'

function replacePolishCharacters(str) {
  const map = {
      'ą': 'a', 'ć': 'c', 'ę': 'e', 'ł': 'l', 'ń': 'n', 'ó': 'o', 'ś': 's', 'ż': 'z', 'ź': 'z',
      'Ą': 'A', 'Ć': 'C', 'Ę': 'E', 'Ł': 'L', 'Ń': 'N', 'Ó': 'O', 'Ś': 'S', 'Ż': 'Z', 'Ź': 'Z'
  };

  return str.split('').map(ch => map[ch] || ch).join('');
}

const Sidebar = () => {
  const { activeMenu, setActiveMenu, screenSize, currentColor, handleClick, isClicked } = useStateContext();

  const handleCloseSidear = () => {
    if (activeMenu && screenSize <= 900) {
      setActiveMenu(false);
    }
  };

  const activeLink = 'flex items-center gap-5 pl-5 pt-3 pb-2.5 text-white text-md';
  const normalLink = 'flex items-center gap-5 pl-5 pt-3 pb-2.5 text-md text-gray-700 dark:hover:text-black hover:bg-light-gray';


  return (
    <div className='h-screen md:overflow-hidden overflow-auto md:hover:overflow-auto bg-white'>
      {activeMenu && (
        <>
          <div className='flex justify-between items-center'>
            <TooltipComponent content="Menu" position="BottomCenter">
              <button
                type='button'
                onClick={() => setActiveMenu((prevActiveMenu) => !prevActiveMenu)}
                className='text-xl rounded-full p-3 hover:bg-light-gray mt-4 block md:hidden'
              >
                <MdOutlineCancel />
              </button>
            </TooltipComponent>
          </div>


          <div
          className='items-center
          cursor-pointer p-1 pt-3 pb-3 bg-neutral-100'
          onClick={() => handleClick
          ('userProfile')}>
            <p>
                <span className='text-gray-800 ml-4 text-20 font-bold'> Na Fali </span>
            </p>
            <p>
                <span className='text-gray-500 ml-4 text-14'> Kamil Pawłowski </span>
            </p>
          </div>

          <div>
            {links.map((link) => (
              <NavLink
              to={`/${replacePolishCharacters(link.name)}`}
              key={link.name}
              onClick={handleCloseSidear}
              style={({ isActive }) => ({
                backgroundColor: isActive ? currentColor : '',
                color: isActive ? 'white' : currentColor
              })}
              className={({ isActive }) => (isActive ? activeLink : normalLink)}
            >
              {link.icon}
              <span className='capitalize'>{link.name}</span>
            </NavLink>
            
            ))}
            {isClicked.UserProfile && <UserProfile />}
          </div>
        </>
      )}
    </div>
  );
};

export default Sidebar;
