import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useLocation  } from 'react-router-dom';
import { HiHome } from 'react-icons/hi';
import { useStateContext } from '../contexts/ContextProvider';
import { Link, NavLink } from 'react-router-dom';
        

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setActiveMenu, currentColor } = useStateContext();

  useEffect(() => {
    if (location.pathname === '/login') {
      setActiveMenu(false);
    }

    const verifyToken = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          await axios.get('http://localhost:8081/api/v1/auth/verify', {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          navigate('/dashboard');
        } catch (error) {
          console.error('Token verification failed:', error);
          localStorage.removeItem('token');
          // Tutaj nie przekierowujemy, ponieważ jesteśmy już na stronie logowania
        }
      }
    };

    verifyToken();
  }, [navigate]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const response = await axios.post('http://localhost:8081/api/v1/auth/authenticate', {
        login: username,
        password: password
      });

      if (response.data && response.data.token) {
        localStorage.setItem('token', response.data.token);
        navigate('/dashboard');
      } else {
        setError('Nie otrzymano tokenu, sprawdź odpowiedź z serwera.');
      }
    } catch (err) {
      console.error('Błąd logowania:', err);
      if (err.response && err.response.data) {
        setError(err.response.data.message || 'Logowanie nieudane. Sprawdź dane logowania i spróbuj ponownie.');
      } else {
        setError('Problem z połączeniem z serwerem.');
      }
    }
  };

  const handleRegister = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const response = await axios.post('http://localhost:8081/api/v1/auth/register', {
        login: username,
        password: password,
        role: { id: 1 } // Tutaj zahardkodowano rolę jako przykład, wartość "1" powinna być zastąpiona odpowiednią wartością
      });

      if (response.data) {
        // Możesz dodać jakąś logikę po pomyślnej rejestracji, np. komunikat powitalny
        alert('Rejestracja pomyślna!');
      } else {
        setError('Rejestracja nieudana, sprawdź odpowiedź z serwera.');
      }
    } catch (err) {
      console.error('Błąd rejestracji:', err);
      if (err.response && err.response.data) {
        setError(err.response.data.message || 'Rejestracja nieudana. Sprawdź dane i spróbuj ponownie.');
      } else {
        setError('Problem z połączeniem z serwerem.');
      }
    }
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh', // Pełna wysokość widoku
      width: '100vw', // Pełna szerokość widoku
      background: '#F8FAFC'
    }}>
      <div style={{
        width: '320px', // Ustaw szerokość diva na 320px
        maxWidth: '320px', // Ustaw maksymalną szerokość diva na 320px
        margin: '0 auto', // Centrowanie diva w poziomie
        padding: '20px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        borderRadius: '8px',
        background: 'white',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center', // Centrowanie zawartości wewnętrznej diva
      }}>
        <div style={{ marginBottom: '20px', textAlign: 'center' }}>
        <div className='items-center gap-3 flex text-2xl font-extrabold tracking-tight dark:text-white text-slate-900 justify-center'> 
              <HiHome size={40} color={ currentColor }/>
              <span style={{ color: currentColor }}>HolidayHouse</span>
          </div>
        </div>
        {error && <div style={{ marginBottom: '20px', color: 'red', textAlign: 'center' }}>{error}</div>}
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '20px' }}>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Login"
              style={{ width: '280px', padding: '10px', fontSize: '16px', border: '1px solid #ddd', borderRadius: '4px' }}
            />
          </div>
          <div style={{ marginBottom: '20px' }}>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Hasło"
              style={{ width: '100%', padding: '10px', fontSize: '16px', border: '1px solid #ddd', borderRadius: '4px' }}
            />
          </div>
          <div>
            <button type="submit" style={{ width: '100%', padding: '10px 0', fontSize: '16px', borderRadius: '4px', border: 'none', color: 'white', background: '#181818', cursor: 'pointer' }}>
              Zaloguj się
            </button>
            <div style={{ marginTop: '10px' }}>
              <button onClick={handleRegister} type="button" style={{ width: '100%', padding: '10px 0', fontSize: '16px', borderRadius: '4px', border: 'none', color: 'white', background: '#375EBC', cursor: 'pointer' }}>
                Zarejestruj się
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
