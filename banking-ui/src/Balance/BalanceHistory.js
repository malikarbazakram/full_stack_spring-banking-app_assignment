import React, { useState, useEffect } from "react";
import axios from "axios";


const BalanceHistoryCard = ({ balance }) => (
  <div className="card border-dark mb-3 " style={{ maxWidth: "1080px" }}>
    <div className="row g-0">
      
      <div className="col-md-4 d-flex align-items-center justify-content-center">
        <img style={{ width: 225, height: 225 }}  src="/bank_card_u.jpg" className="img-fluid rounded-start" alt="Picture"/>
      </div>

      <div className="col-md-8">
        <div className="card-body">
          <h2 className="card text-bg-dark mb-3 align-items-center justify-content-center">Details</h2>
          <div className="card-body card text-bg-light mb-3 align-items-center justify-content-center">
            <h2  className="card-title"><b>Balance Id:</b>{' '}{balance.balance_id}</h2>
            <h3 className="card-text"><b>Date:</b>{' '} {balance.date}</h3>  
            <h3 className="card-text"><b>Amount:</b>{' '} {balance.amount}</h3>
            <h3 className="card-text"><b>Balance Type:</b>{' '} {balance.balanceType}</h3>
          </div>
        </div>
      </div>

    </div>
  </div>
);

const BalanceHistory = () => {
  const [balanceHistory, setBalanceHistory] = useState([]);

  useEffect(() => {
    loadBalanceHistory();
  }, []);

  const loadBalanceHistory = async () => {
    try {
      const response = await axios.get("/api/v1/balance", {
        withCredentials: true,
        headers: {
          Authorization: "Basic " + btoa("admin:admin"),
        },
      });
      setBalanceHistory(response.data.content);
    } catch (error) {
      console.error("Error loading balance history:", error);
    }
  };

  return (
    <div className="container">
      <h2 className="mt-4 mb-4" style={{ color: "white" }}>Balance History</h2>
      <div className="row">
        {balanceHistory.length === 0 ? (
          <p> </p>
        ) : (
          balanceHistory.map((balance) => (
            <BalanceHistoryCard key={balance.balance_id} balance={balance} />
          ))
        )}
      </div>
    </div>
  );
};

export default BalanceHistory;
