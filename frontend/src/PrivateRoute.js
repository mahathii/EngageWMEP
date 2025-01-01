// src/PrivateRoute.js

import React from "react";
import { Navigate, Outlet } from "react-router-dom";

const PrivateRoute = () => {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    return isLoggedIn ? <Outlet /> : <Navigate to="/signup" />;
};

export default PrivateRoute;