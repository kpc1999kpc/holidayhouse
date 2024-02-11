import React, { useEffect } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import axios from 'axios';

const ProtectedRoute = ({ children }) => {
  const navigate = useNavigate();
  const token = localStorage.getItem('token'); // Przenieś tę linię na początek komponentu
  
  useEffect(() => {
    // Nie musisz już tutaj deklarować token, ponieważ jest on dostępny w zakresie komponentu
    if (token) {
      const verifyToken = async () => {
        try {
          await axios.get('http://localhost:8081/api/v1/auth/verify', {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          // Token jest ważny
        } catch (error) {
          // Token jest nieważny lub wystąpił błąd
          localStorage.removeItem('token');
          navigate('/dashboard');
        }
      };

      verifyToken();
    } else {
      navigate('/login');
    }
  }, [navigate, token]); // Dodaj token do listy zależności useEffect

  // Token jest już dostępny, więc możemy go użyć bezpośrednio w return statement
  return token ? children : <Navigate to="/login" replace />;
};

export default ProtectedRoute;