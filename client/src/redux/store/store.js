import { configureStore } from "@reduxjs/toolkit";
import cartSlice from "./shopping-cart/cartSlice";
import cartUISlice from "./shopping-cart/cartUISlice";

const store = configureStore({
	reducer: {
		cart: cartSlice,
		cartUI: cartUISlice,
	},
});

export default store;
