import React, { useState } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AddAccountForm = ({ onAdd, onClose}) => {
  const [newAccount, setNewAccount] = useState({
    name: '',
    email: '',
    address: '',
    username: '',
    password: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewAccount({
      ...newAccount,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Send a POST request to create a new account with the password as a parameter
      const response = await axios.post('/api/v1/accounts', newAccount, {
        params: {
        //   username: username,
        //   password: password, // Pass the password as a parameter
        },
        withCredentials: true,
        headers: {
          'Authorization': 'Basic ' + btoa('admin:admin'),
        },
      });

      if (response.status === 200) {
       // Show a success toast notification
        toast.success('Account created successfully!', {
          position: toast.POSITION.BOTTOM_RIGHT,
        });
        
        // Call the onAdd function to add the new account to the list
        onAdd(response.data);

        // Clear the form fields and password
        setNewAccount({
          name: '',
          email: '',
          address: '',
          username: '',
          password: '',
        });
        window.location.reload();
        onClose();
        // setPassword('');
        // setUsername('');
      } else {
        // Show an error toast notification
        toast.error('Error creating account. Please try with different username.', {
          position: toast.POSITION.BOTTOM_RIGHT,
        });
      }
      onClose();
      
    } catch (error) {
      // Handle errors and show an error toast notification
      toast.error('Error creating account. Please try with different username.', {
        position: toast.POSITION.BOTTOM_RIGHT,
      });
      console.error('Error creating account:', error);
    }
  };

  return (
    <div className="add-account-form">

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="name" className="form-label"  style={{ color: 'white' }}>
            Name
          </label>
          <input
            type="text"
            className="form-control"
            id="name"
            name="name"
            value={newAccount.name}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
            required
          />
        </div>
                <div className="mb-3">
          <label htmlFor="username" className="form-label" style={{ color: 'white' }}>
            Username
          </label>
          <input
            type="text" // Use type="password" for password input
            className="form-control"
            id="username"
            name="username"
            value={newAccount.username}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label" style={{ color: 'white' }}>
            Email
          </label>
          <input
            type="email"
            className="form-control"
            id="email"
            name="email"
            value={newAccount.email}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label" style={{ color: 'white' }}>
            Password
          </label>
          <input
            type="password" // Use type="password" for password input
            className="form-control"
            id="password"
            name="password"
            value={newAccount.password}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
            required
          />
        </div>
        <div className="mb-3">
          <label htmlFor="address" className="form-label" style={{ color: 'white' }}>
            Address
          </label>
          <input
            type="text"
            className="form-control"
            id="address"
            name="address"
            value={newAccount.address}
            onChange={handleInputChange}
            style={{ maxWidth: '500px' }}
            required
          />
        </div>
        <button type="submit" className="btn btn-dark" style={{ marginRight: '315px' }}>
          Create Account
        </button>
        <button type="button" className="btn btn-danger" onClick={onClose}>
        Cancel
      </button>
        
        <br />
      <br />
      </form>
      <ToastContainer />
    </div>
  );
};

export default AddAccountForm;