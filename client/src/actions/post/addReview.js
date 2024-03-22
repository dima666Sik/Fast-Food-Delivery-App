import axios from "axios";

export const addReview = async (accessToken, dataProductReview) => {
	try {
		const response = axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/food-reviews/add-review-for-product`,
			dataProductReview,
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		return response;
	} catch (error) {
		throw error;
	}
};
