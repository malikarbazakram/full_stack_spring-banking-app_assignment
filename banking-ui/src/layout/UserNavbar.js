import React, { useState, useEffect } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { Link } from "react-router-dom";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import ViewAccounts from "../Account/ViewAccount";
import TransactionForm from "../Transaction/TransactionForm";
import TransferFunds from "../Transaction/TransferFunds";
import TransactionHistory from "../Transaction/TransactionHistory";
import BalanceHistory from "../Balance/BalanceHistory";
import ViewUserAccountDetails from "../Account/VewUserAccountDetails";
const UserNavbar = () => {
  const handleLogout = () => {
    window.location.href = "http://localhost:3000/login";
  };
  return (
    <div className="App">
      <Router>
        <nav
          className="navbar navbar-expand-lg bg-body-tertiary"
          data-bs-theme="black"
        >
          <div className="container-fluid">
            <Link className="navbar-brand mb-0 h1" to="/">
              <b>Revolut</b>
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon" />
            </button>
            <div
              className="collapse navbar-collapse"
              id="navbarSupportedContent"
            >
              <ul
                className="navbar-nav me-auto mb-2 mb-lg-0"
                data-bs-theme="dark"
              >
                <li className="nav-item">
                  <Link className="nav-link active" aria-current="page" to="/">
                    Home
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/BalanceHistory">
                    Balance History
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/TransactionHistory">
                    Transaction History
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/TransactionForm">
                    Transaction
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/TransferFunds">
                    Transfer Funds
                  </Link>
                </li>

                <li className="nav-item">
                  <button className="nav-link" onClick={handleLogout}>
                    Logout
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </nav>

        <Routes>
          <Route path="/" element={<ViewUserAccountDetails />} />
          <Route path="/BalanceHistory" element={<BalanceHistory />} />
          <Route path="/TransactionHistory" element={<TransactionHistory />} />
          <Route path="/TransactionForm" element={<TransactionForm />} />
          <Route path="/TransferFunds" element={<TransferFunds />} />
          <Route path="/Logout" />
          <Route path="/ViewAccounts" element={<ViewAccounts />} />
        </Routes>
      </Router>
    </div>
  );
};

const AdminNavbar = () => {
  const handleLogout = () => {
    window.location.href = "http://localhost:3000/login";
  };
  return (
    <div className="App">
      <Router>
        <nav
          className="navbar navbar-expand-lg bg-body-tertiary"
          data-bs-theme="black"
        >
          <div className="container-fluid">
            <Link className="navbar-brand mb-0 h1" to="/">
              <b>Revolut</b>
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon" />
            </button>
            <div
              className="collapse navbar-collapse"
              id="navbarSupportedContent"
            >
              <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                <li className="nav-item">
                  <Link className="nav-link active" aria-current="page" to="/">
                    Home
                  </Link>
                </li>
                <li className="nav-item">
                  <button className="nav-link" onClick={handleLogout}>
                    Logout
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </nav>

        <Routes>
          <Route path="/" element={<ViewAccounts />} />
          <Route path="/Logout" />
        </Routes>
      </Router>
    </div>
  );
};

const Navbar = () => {
  const [userRole, setUserRole] = useState(null);

  useEffect(() => {
    // Create headers object with the desired headers
    const headers = {
      Authorization: "Basic " + btoa("admin:admin")
    };

    // Make an axios request to get the user's role with the specified headers
    axios
      .get("/api/v1/accounts/role", {
        withCredentials: true,
        headers: headers
      })
      .then(response => {
        console.log(response);
        setUserRole(response.data);
      })
      .catch(error => {
        console.error("Error fetching user role:", error);
      });
  }, []);

  return (
    <div className="App">
      {userRole === "ROLE_ADMIN" ? <AdminNavbar /> : <UserNavbar />}
    </div>
  );
};

export default Navbar;
