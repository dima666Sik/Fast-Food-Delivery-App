import { createSlice } from "@reduxjs/toolkit";
import Cookies from "js-cookie";
import CryptoJS from "crypto-js";

const encryptData = (data) => {
	const encryptedData = CryptoJS.AES.encrypt(
		JSON.stringify(data),
		process.env.REACT_APP_SECRET_KEY_CRYPTO_JS
	).toString();
	return encryptedData;
};

const decryptData = (encryptedData) => {
	try {
		const bytes = CryptoJS.AES.decrypt(
			encryptedData,
			process.env.REACT_APP_SECRET_KEY_CRYPTO_JS
		);
		const decryptedData = JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
		return decryptedData;
	} catch (error) {
		clearCookie("cartItems");
		clearCookie("totalAmount");
		clearCookie("totalQuantity");
		console.log("Error decrypt data...");
		// Дополнительные действия при ошибке расшифровки
	}
};

const getCookie = (name) => {
	const encryptedCookie = Cookies.get(name);
	if (encryptedCookie) {
		const decryptedCookie = decryptData(encryptedCookie);
		return decryptedCookie;
	}
	return null;
};

const setCookie = (name, value) => {
	const encryptedValue = encryptData(value);
	Cookies.set(name, encryptedValue, { expires: 7 });
};

const clearCookie = (name) => {
	Cookies.remove(name);
};

const initialState = {
	cartItems: getCookie("cartItems") || [],
	totalQuantity: getCookie("totalQuantity") || 0,
	totalAmount: getCookie("totalAmount") || 0,
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

			setCookie("cartItems", state.cartItems);
			setCookie("totalAmount", state.totalAmount);
			setCookie("totalQuantity", state.totalQuantity);
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

			setCookie("cartItems", state.cartItems);
			setCookie("totalAmount", state.totalAmount);
			setCookie("totalQuantity", state.totalQuantity);
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

			setCookie("cartItems", state.cartItems);
			setCookie("totalAmount", state.totalAmount);
			setCookie("totalQuantity", state.totalQuantity);
		},

		clearCart(state) {
			state.cartItems = [];
			state.totalQuantity = 0;
			state.totalAmount = 0;

			clearCookie("cartItems");
			clearCookie("totalAmount");
			clearCookie("totalQuantity");
		},
	},
});

export const cartActions = cartSlice.actions;
export default cartSlice.reducer;
