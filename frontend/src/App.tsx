import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import './styles/global.css';
import Navbar from './components/Navbar';
import PortfolioPage from './pages/PortfolioPage';
import LoginPage from './pages/LoginPage';
import { AUTH_CONFIG } from './config/auth';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return AUTH_CONFIG.token ? <>{children}</> : <Navigate to="/login" />;
};

const App: React.FC = () => {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/portfolio"
            element={
              <PrivateRoute>
                <PortfolioPage />
              </PrivateRoute>
            }
          />
          <Route path="/" element={<Navigate to="/portfolio" />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
