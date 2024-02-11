import React from 'react';
import { useLocation } from 'react-router-dom';
import { Navbar, Sidebar, ThemeSettings } from './'; // Upewnij się, że importujesz z odpowiedniej ścieżki
import { useStateContext } from '../contexts/ContextProvider';
import { FiSettings } from 'react-icons/fi';
import { TooltipComponent } from '@syncfusion/ej2-react-popups';


const LayoutWrapper = ({ children }) => {
  const location = useLocation();
  const { activeMenu, themeSettings, setThemeSettings, currentColor, currentMode } = useStateContext();

  if (location.pathname === '/login') {
    return <>{children}</>; // Tylko zawartość przekazana do LayoutWrapper będzie renderowana, bez Navbar i Sidebar
  }

  return (
    <>
      <Navbar />
      <div className='fixed right-4 bottom-4' style={{zIndex:'1000'}}>
            <TooltipComponent content='Ustawienia' position='TopCenter'>
              <button type='button'
              className='text-3xl p-3
              hover:drop-shadow-x1
              hover:bg-light-gray
              text-white'
              onClick={() => setThemeSettings(true)}
              style={{background: currentColor,
              borderRadius: '50%'}}>
                <FiSettings />
              </button>
            </TooltipComponent>
      </div>
      { themeSettings && <ThemeSettings />}
    </>
  );
};

export default LayoutWrapper;
