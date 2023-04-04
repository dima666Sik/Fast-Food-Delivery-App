import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import Home from "../pages/Home";
import FoodDetails from "../pages/food-details/FoodDetails";
import Cart from "../pages/Cart";
import Contact from "../pages/Contact";
import AllFoods from "../pages/all-foods/AllFoods";
// import Checkout from "../pages/Checkout";

const Routers = () => {
	return (
		<Routes>
			<Route path="/" element={<Navigate to="/home" />} />
			<Route path="/home" element={<Home />} />
			<Route path="/foods" element={<AllFoods />} />
			<Route path="/foods/:id" element={<FoodDetails />} />
			<Route path="/cart" element={<Cart />} />
			<Route path="/contact" element={<Contact />} />
			<Route path="*" element={<Navigate to="/" />} />
		</Routes>
	);
};

export default Routers;
