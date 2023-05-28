import { configureStore } from "@reduxjs/toolkit";

import cartSlice from "./shopping-cart/cartSlice";
import cartUISlice from "./shopping-cart/cartUISlice";
import userSlice from "./user/userSlice";
import cartsLikedSlice from "./shopping-cart/cartsLikedSlice";

const store = configureStore({
	reducer: {
		cart: cartSlice,
		cartUI: cartUISlice,
		user: userSlice,
		cartsLiked: cartsLikedSlice,
	},
});

export default store;
