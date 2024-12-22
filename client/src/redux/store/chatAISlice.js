import { createSlice } from "@reduxjs/toolkit";

const chatAISlice = createSlice({
	name: "chatAI",
	initialState: { isChatVisible: false },
	reducers: {
		toggleVisible(state) {
			state.isChatVisible = !state.isChatVisible;
		},
	},
});

export const chatAIActions = chatAISlice.actions;
export default chatAISlice.reducer;
