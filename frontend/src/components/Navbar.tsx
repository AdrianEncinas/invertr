import React from 'react';
import { Link } from 'react-router-dom';

const Navbar: React.FC = () => {
  return (
    <nav className="nav">
      <div className="container nav-content">
        <Link to="/" className="logo">
          Invertrack
        </Link>
        <div className="nav-links">
          <Link to="/" className="btn">
            Portfolio
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navbar; 