import { createSlice } from "@reduxjs/toolkit";

// const items =
// 	localStorage.getItem("cartItems") !== null
// 		? JSON.parse(localStorage.getItem("cartItems"))
// 		: [];

// const totalAmount =
// 	localStorage.getItem("totalAmount") !== null
// 		? JSON.parse(localStorage.getItem("totalAmount"))
// 		: 0;

// const totalQuantity =
// 	localStorage.getItem("totalQuantity") !== null
// 		? JSON.parse(localStorage.getItem("totalQuantity"))
// 		: 0;

// const setItemFunc = (item, totalAmount, totalQuantity) => {
// 	localStorage.setItem("cartItems", JSON.stringify(item));
// 	localStorage.setItem("totalAmount", JSON.stringify(totalAmount));
// 	localStorage.setItem("totalQuantity", JSON.stringify(totalQuantity));
// };

// const initialState = {
// 	cartItems: items,
// 	totalQuantity: totalQuantity,
// 	totalAmount: totalAmount,
// };

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
				const { id, title, image01, price } = newItem;
				state.cartItems = [
					...state.cartItems,
					{ id, title, image01, price, quantity: 1, totalPrice: price },
				];
			} else {
				existItem.quantity++;
				existItem.totalPrice =
					Number(existItem.totalPrice) + Number(newItem.price);
			}

			state.totalAmount = state.cartItems.reduce(
				(total, item) => total + Number(item.price) * Number(item.quantity),
				0
			);

			// setItemFunc(
			// 	state.cartItems.map((item) => item),
			// 	state.totalAmount,
			// 	state.totalQuantity
			// );
		},

		removeItem(state, action) {
			const id = action.payload;
			const existingItem = state.cartItems.find((item) => item.id === id);
			state.totalQuantity--;

			if (existingItem.quantity === 1) {
				state.cartItems = state.cartItems.filter((item) => item.id !== id);
			} else {
				existingItem.quantity--;
				existingItem.totalPrice =
					Number(existingItem.totalPrice) - Number(existingItem.price);
			}

			state.totalAmount = state.cartItems.reduce(
				(total, item) => total + Number(item.price) * Number(item.quantity),
				0
			);

			// setItemFunc(
			// 	state.cartItems.map((item) => item),
			// 	state.totalAmount,
			// 	state.totalQuantity
			// );
		},

		deleteItem(state, action) {
			const id = action.payload;
			const existingItem = state.cartItems.find((item) => item.id === id);

			if (existingItem) {
				state.cartItems = state.cartItems.filter((item) => item.id !== id);
				state.totalQuantity = state.totalQuantity - existingItem.quantity;
			}

			state.totalAmount = state.cartItems.reduce(
				(total, item) => total + Number(item.price) * Number(item.quantity),
				0
			);
			// setItemFunc(
			// 	state.cartItems.map((item) => item),
			// 	state.totalAmount,
			// 	state.totalQuantity
			// );
		},
	},
});

export const cartActions = cartSlice.actions;
export default cartSlice.reducer;
