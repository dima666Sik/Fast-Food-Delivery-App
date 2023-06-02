import { createSlice } from "@reduxjs/toolkit";

const basketUISlice = createSlice({
	name: "basketUI",
	initialState: { basketIsVisible: false },
	reducers: {
		toggleVisible(state) {
			state.basketIsVisible = !state.basketIsVisible;
		},
	},
});

export const basketUIActions = basketUISlice.actions;
export default basketUISlice.reducer;
