import { createSlice } from "@reduxjs/toolkit";

const initialState = {
	cartItems: [],
	totalQuantity: 0,
	totalAmount: 0,
};

const cartSlice = createSlice({
	name: "cart",
	initialState: initialState,
	reducers: {
		addItem(state, action) {
			const newItem = action.payload;
			const existItem = state.cartItems.find((item) => item.id === newItem.id);
			state.totalQuantity++;

			if (!existItem) {
				state.cartItems.push({
					id: newItem.id,
					title: newItem.title,
					image01: newItem.image01,
					price: newItem.price,
					quantity: 1,
					totalPrice: newItem.price,
				});
			} else {
				existItem.quantity++;
				existItem.totalPrice =
					Number(existItem.totalPrice) + Number(newItem.price);
			}

			state.totalAmount = state.cartItems.reduce(
				(total, item) => total + Number(item.price) * Number(item.quantity)
			);
		},
	},
});

export const cartActions = cartSlice.actions;
export default cartSlice;
