import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Navbar, Footer, Sidebar, ThemeSettings, ProtectedRoute, Login, LayoutWrapper } from './components'
import { Orders, Calendar, Employees, Pyramid, Kanban, Area, Bar,
Pie, Line, Financial, ColorPicker, ColorMapping, Editor, Customers, Houses, Reservations, Payments, Dashboard} from './pages'
import { useStateContext } from './contexts/ContextProvider';
import './App.css';


const App = () => {
  const { activeMenu, themeSettings, setThemeSettings, currentColor, currentMode} = useStateContext()

  return (
    <div className={currentMode === 'Dark' ? 'dark' : ''}>
      <BrowserRouter>
        <LayoutWrapper />
        <div className='flex relative dark:bg:main-darl-bg'>
          
          {activeMenu ? (
            <div className='w-72 fixed sidebar
            dark:bg-secondary-dark-bg bg
            bg-white'>
              <Sidebar />
            </div>
          ) : (
            <div className='w-0 fixed
            dark:bg-secondary-dark-bg'>
              <Sidebar />
            </div>
          )}
          <div className={
              `dark:bg-main-dark-bg bg-slate-50 w-full
              ${activeMenu
                ? 'md:ml-72'
                : 'flex-0'}`}
          style={{ minHeight: '739px',
          }}>
            
            <Routes>
              <Route path="/login" element={<Login />} />

              {/* Dashboard */}
              <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
              <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
              
              {/* Pages */}
              <Route path="/rezerwacje" element={<ProtectedRoute><Reservations /></ProtectedRoute>} />
              <Route path="/platnosci" element={<ProtectedRoute><Payments /></ProtectedRoute>} />
              <Route path="/domki" element={<ProtectedRoute><Houses /></ProtectedRoute>} />
              <Route path="/klienci" element={<ProtectedRoute><Customers /></ProtectedRoute>} />

              <Route path="/orders" element={<ProtectedRoute><Orders /></ProtectedRoute>} />
              <Route path="/pracownicy" element={<ProtectedRoute><Employees /></ProtectedRoute>} />
              
              {/* Apps */}
              <Route path="/kanban" element={<ProtectedRoute><Kanban /></ProtectedRoute>} />
              <Route path="/edytor" element={<ProtectedRoute><Editor /></ProtectedRoute>} />
              <Route path="/kalendarz" element={<ProtectedRoute><Calendar /></ProtectedRoute>} />
              <Route path="/color-picker" element={<ProtectedRoute><ColorPicker /></ProtectedRoute>} />
              
              {/* Charts */}
              <Route path="/line" element={<ProtectedRoute><Line /></ProtectedRoute>} />
              <Route path="/area" element={<ProtectedRoute><Area /></ProtectedRoute>} />
              <Route path="/bar" element={<ProtectedRoute><Bar /></ProtectedRoute>} />
              <Route path="/pie" element={<ProtectedRoute><Pie /></ProtectedRoute>} />
              <Route path="/financial" element={<ProtectedRoute><Financial /></ProtectedRoute>} />
              <Route path="/color-mapping" element={<ProtectedRoute><ColorMapping /></ProtectedRoute>} />
              <Route path="/pyramid" element={<ProtectedRoute><Pyramid /></ProtectedRoute>} />
              <Route path="*" element={<Navigate replace to="/dashboard" />} />
            </Routes>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
};

export default App;