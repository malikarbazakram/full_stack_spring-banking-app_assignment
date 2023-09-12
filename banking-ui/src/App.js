import "../node_modules/bootstrap/dist/css/bootstrap.min.css"
import React from 'react';
import "../node_modules/bootstrap/dist/js/bootstrap.min.js"
import Navbar from './layout/UserNavbar';
import './App.css';


function App() {
  return (
    <div className="App" style={{ backgroundImage: '/src/layout/app_bg_.png' }}>
      <Navbar />
    </div>
  );
}

export default App;
