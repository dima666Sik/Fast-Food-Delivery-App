import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

import { setLike, setStatus } from "../../../actions/post/setLikes";
import { getListStatusForUser } from "../../../actions/get/getLikes";
import { clearUser, setUser } from "../user/userSlice";
import { cartActions } from "./cartSlice";

const initialState = {
	listCartsLiked: [],
};

export const axiosSetLikeAndStatus = createAsyncThunk(
	"cartsLiked/axiosSetLikeAndStatus",
	async (
		{ id, likes, accessToken },
		{ rejectWithValue, getState, dispatch }
	) => {
		try {
			const existItemIndex = getState().cartsLiked.listCartsLiked.findIndex(
				(item) => item.id === id
			);
			if (existItemIndex !== -1) {
				const newStatus =
					!getState().cartsLiked.listCartsLiked[existItemIndex].status;

				const currentLikes =
					getState().cartsLiked.listCartsLiked[existItemIndex].likes;

				const newLike = newStatus ? currentLikes + 1 : currentLikes - 1;

				await setLike(id, newLike, accessToken);

				await setStatus(id, newStatus, accessToken);

				dispatch(
					cartActionsLiked.editElementListLikes({
						existItemIndex,
						status: newStatus,
						likes: newLike,
					})
				);
				return { id, newStatus, newLike };
			} else {
				const newCountLikes = likes + 1;
				const newStatus = true;
				await setLike(id, newCountLikes, accessToken);
				await setStatus(id, newStatus, accessToken);

				dispatch(
					cartActionsLiked.addElementIntoListLikes({
						id,
						status: newStatus,
						likes: newCountLikes,
					})
				); // Обновляем состояние Redux
				return { id, newStatus, likes };
			}
		} catch (error) {
			if (error.response.data.access_token) {
				// Dispatch the setUser action
				dispatch(
					setUser({
						accessToken: error.response.data.access_token,
					})
				);
			}

			if (
				error.response.data ===
					"You need to reauthorize! Tokens all were expired. You will be much to authorization!" ||
				error.response.data === "Valid Refresh token was expired..."
			) {
				dispatch(clearUser());
				dispatch(cartActions.clearCart());
				dispatch(cartActionsLiked.clearCartsLiked());
			}

			return rejectWithValue({
				message: error.message,
				code: error.code,
				response_data: error.response?.data,
				response_status: error.response?.status,
			});
		}
	}
);

export const axiosGetStatusLikes = createAsyncThunk(
	"cartsLiked/axiosGetStatusLikes",
	async ({ accessToken }, { rejectWithValue, dispatch }) => {
		try {
			const resultListStatus = await getListStatusForUser(accessToken);
			dispatch(
				cartActionsLiked.addListExistStatusesProducts({
					resultListStatus,
				})
			); // Обновляем состояние Redux
		} catch (error) {
			if (error.response.data.access_token) {
				console.log("+=+");
				// Dispatch the setUser action
				dispatch(
					setUser({
						accessToken: error.response.data.access_token,
					})
				);
			}
			console.log(error.response.data);
			if (
				error.response.data ===
					"You need to reauthorize! Tokens all were expired. You will be much to authorization!" ||
				error.response.data === "Valid Refresh token was expired..."
			) {
				dispatch(clearUser());
				dispatch(cartActions.clearCart());
				dispatch(cartActionsLiked.clearCartsLiked());
			}

			return rejectWithValue({
				message: error.message,
				code: error.code,
				response_data: error.response?.data,
				response_status: error.response?.status,
			});
		}
	}
);

const cartsLikedSlice = createSlice({
	name: "cartsLiked",
	initialState,
	reducers: {
		clearCartsLiked(state) {
			state.listCartsLiked = [];
		},
		addElementIntoListLikes(state, action) {
			const { id, status, likes } = action.payload;

			state.listCartsLiked.push({
				id,
				status: status,
				likes: likes,
			});
		},
		editElementListLikes(state, action) {
			const { existItemIndex, status, likes } = action.payload;

			state.listCartsLiked[existItemIndex].status = status;
			state.listCartsLiked[existItemIndex].likes = likes;
		},
		addListExistStatusesProducts(state, action) {
			const { resultListStatus } = action.payload;
			state.listCartsLiked = [...state.listCartsLiked, ...resultListStatus];
		},
	},
	extraReducers: (builder) => {
		builder.addCase(axiosSetLikeAndStatus.fulfilled, (state, action) => {
			console.log(state, action);
		});
		builder.addCase(axiosGetStatusLikes.fulfilled, (state, action) => {
			console.log(state, action);
		});
		builder.addCase(axiosSetLikeAndStatus.rejected, (state, action) => {
			if (action?.payload?.code === "ERR_NETWORK") {
				console.log("Error network, please try again...");
				return;
			}
			console.log(action.payload); // Handle the error
		});
		builder.addCase(axiosGetStatusLikes.rejected, (state, action) => {
			console.log(action.payload); // Handle the error
		});
	},
});

export const cartActionsLiked = cartsLikedSlice.actions;
export default cartsLikedSlice.reducer;
