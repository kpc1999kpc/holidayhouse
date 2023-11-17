import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { FiSettings } from 'react-icons/fi';
import { TooltipComponent } from '@syncfusion/ej2-react-popups';
import { Navbar, Footer, Sidebar, ThemeSettings } from './components'
import { Ecommerce, Orders, Calendar, Employees,
Pracowynicy, Pyramid, Kanban, Area, Bar,
Pie, Line, Financial, ColorPicker, ColorMapping, Editor, Customers, Houses, Reservations, Payments} from './pages'


import { useStateContext } from './contexts/ContextProvider';

import './App.css';


const App = () => {
  const { activeMenu, themeSettings, setThemeSettings, currentColor, currentMode} = useStateContext()

  return (
    <div className={currentMode === 'Dark' ? 'dark' : ''}>
      <BrowserRouter>
        <Navbar />
        <div className='flex relative dark:bg:main-darl-bg'>
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
          {activeMenu ? (
            <div className='w-72 fixed sidebar
            dark:bg-secondary-dark-bg bg
            bg-white'>
              <Sidebar />
            </div>
          ) : (
            <div className='w-0
            dark:bg-secondary-dark-bg'>
              <Sidebar />
            </div>
          )}
          <div className={
              `dark:bg-main-dark-bg bg-main-bg w-full 
              ${activeMenu
                ? 'md:ml-72'
                : 'flex-0'}`}
          style={{ minHeight: '650px',
          }}>
            { themeSettings && <ThemeSettings />}
            <Routes>
              {/* Dashboard */}
              <Route path="/" element={<Ecommerce />} />
              <Route path="/dashboard" element={<Ecommerce />} />
              
              {/* Pages */}
              <Route path="/rezerwacje" element={<Reservations />} />
              <Route path="/platnosci" element={<Payments />} />
              <Route path="/domki" element={<Houses />} />
              <Route path="/klienci" element={<Customers />} />

              <Route path="/orders" element={<Orders />} />
              <Route path="/pracownicy" element={<Employees />} />
              
              {/* Apps */}
              <Route path="/kanban" element={<Kanban />} />
              <Route path="/editor" element={<Editor />} />
              <Route path="/kalendarz" element={<Calendar />} />
              <Route path="/color-picker" element={<ColorPicker />} />
              
              {/* Charts */}
              <Route path="/line" element={<Line />} />
              <Route path="/area" element={<Area />} />
              <Route path="/bar" element={<Bar />} />
              <Route path="/pie" element={<Pie />} />
              <Route path="/financial" element={<Financial />} />
              <Route path="/color-mapping" element={<ColorMapping />} />
              <Route path="/pyramid" element={<Pyramid />} />
              

            </Routes>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;
