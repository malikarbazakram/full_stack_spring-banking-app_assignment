import React, { useState } from 'react';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const EditAccountForm = ({ account, onSave, onClose }) => {
  const [editedAccount, setEditedAccount] = useState({ ...account });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditedAccount({
      ...editedAccount,
      [name]: value,
    });
  };

  const handleSave = async () => {
    try {
      // Send a PUT request to update the account information
      await axios.put(`/api/v1/accounts`, editedAccount, {
        withCredentials: true,
        headers: {
          'Authorization': 'Basic ' + btoa('admin:admin'),
        },
      });

      // Call the onSave function to update the account information in the parent component
      onSave(editedAccount);

      // Show a success toast notification
      // toast.warn('Account updated successfully!', {
      //   position: toast.POSITION.TOP_RIGHT,
      // });

      // Close the edit form
      onClose();
    } catch (error) {
      // Show an error toast notification
      toast.error('Error updating account. Please try again.', {
        position: toast.POSITION.TOP_RIGHT,
      });

      console.error('Error updating account:', error);
    }
  };

  return (
    <div className="edit-account-form">
      <h2 style={{ color: 'white' }} >Edit Account</h2>
      <form>
        <div className="mb-3">
          <label htmlFor="email" className="form-label" style={{ color: 'white' }}>
            Email
          </label>
          <input
            type="email"
            className="form-control"
            id="email"
            name="email"
            value={editedAccount.email}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
          />
        </div>
        <div className="mb-3">
          <label htmlFor="name" className="form-label" style={{ color: 'white' }}>
            Name
          </label>
          <input
            type="text"
            className="form-control"
            id="name"
            name="name"
            value={editedAccount.name}
            onChange={handleInputChange}
            style={{ maxWidth: '300px' }}
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
            value={editedAccount.address}
            onChange={handleInputChange}
            style={{ maxWidth: '500px' }}
          />
        </div>
        <button
          type="button"
          className="btn btn-dark"
          onClick={handleSave}
          style={{ marginRight: '400px' }}
        >
          Save
        </button>
        <button
          type="button"
          className="btn btn-danger"
          onClick={onClose}
        >
          Cancel
        </button>
        
      </form>
      <br/>
      <ToastContainer />
    </div>
  );
};
export default EditAccountForm;