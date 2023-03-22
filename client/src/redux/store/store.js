import { configureStore } from "@reduxjs/toolkit";
import cartSlice from "./shopping-cart/cartSlice";

const store = configureStore({
	reducer: {
		cart: cartSlice.reducer,
	},
});

export default store;

// const rootReducer = combineReducers({
// 	score: sliceScore,
// 	users: sliceUsers,
// });

// export const store = configureStore({
// 	reducer: rootReducer,
// });
