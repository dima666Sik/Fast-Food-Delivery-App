import { configureStore } from "@reduxjs/toolkit";

import cartSlice from "./shopping-cart/cartSlice";
import userSlice from "./user/userSlice";
import cartsLikedSlice from "./shopping-cart/cartsLikedSlice";
import basketUISlice from "./shopping-cart/basketUISlice";
import chatAISlice from "./chatAISlice";

const store = configureStore({
	reducer: {
		cart: cartSlice,
		basketUI: basketUISlice,
		chatAI: chatAISlice,
		user: userSlice,
		cartsLiked: cartsLikedSlice,
	},
});

export default store;
