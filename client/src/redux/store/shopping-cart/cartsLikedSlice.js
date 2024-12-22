import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

import { setStatus } from "../../../actions/put/setStatus";
import { getListStatusForUser } from "../../../actions/get/getLikes";
import { axiosLogout, clearUser, setUser } from "../user/userSlice";

import { cartActions } from "./cartSlice";
import { refresh } from "../../../actions/post/refresh";
import { setLike } from "../../../actions/put/setLikes";

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
			console.log(id, likes);
			const existItemIndex = getState().cartsLiked.listCartsLiked.findIndex(
				(item) => item.id === id
			);
			console.log(existItemIndex, id, likes);
			if (existItemIndex !== -1) {
				const newStatus =
					!getState().cartsLiked.listCartsLiked[existItemIndex].status;

				const currentLikes =
					getState().cartsLiked.listCartsLiked[existItemIndex].likes;

				const newLike = newStatus ? currentLikes + 1 : currentLikes - 1;
				console.log(existItemIndex, newStatus, id, currentLikes);
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
			console.log(error, accessToken);
			if (
				error.response.data.message === "Access token has expired" ||
				error.response.data.message === "Access token has revoked"
			) {
				try {
					console.log("refresh");
					const response = await refresh(accessToken);

					if (response.status === 200) {
						if (response.data.access_token) {
							// Dispatch the setUser action
							dispatch(
								setUser({
									accessToken: response.data.access_token,
								})
							);
						}
					}
				} catch (error) {
					console.log(error);
					throw error; // пробрасываем ошибку выше для обработки
				}
			}

			if (
				error.response.data.message ===
					"Access & Refresh tokens have expired" ||
				error.response.data.message === "Access token not found" ||
				error.response.data.message === "Refresh token not found" ||
				error.response.data.message === "Invalid token"
			) {
				console.log("log out");
				dispatch(
					axiosLogout({
						accessToken,
					})
				);
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
			console.log(error.response, accessToken);
			console.log(error.response.data);
			console.log(error.response.data.message);
			if (
				error.response.data.message === "Access token has expired" ||
				error.response.data.message === "Access token has revoked"
			) {
				try {
					console.log("refresh");
					const responseToken = await refresh(accessToken);

					if (responseToken.status === 200) {
						console.log(responseToken);
						if (responseToken.data.access_token) {
							// Dispatch the setUser action
							dispatch(
								setUser({
									accessToken: responseToken.data.access_token,
								})
							);
						}
					}
				} catch (error) {
					console.log(error);
					throw error; // пробрасываем ошибку выше для обработки в setLikes
				}
			}

			if (
				error.response.data.message ===
					"Access & Refresh tokens have expired" ||
				error.response.data.message === "Access token not found" ||
				error.response.data.message === "Refresh token not found" ||
				error.response.data.message === "Invalid token"
			) {
				console.log("log out");
				dispatch(
					axiosLogout({
						accessToken,
					})
				);
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
