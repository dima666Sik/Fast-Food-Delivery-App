import axios from "axios";

export const addReview = async (accessToken, dataProductReview) => {
	try {
		const response = axios.post(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/private/food-reviews/add-review-for-product`,
			dataProductReview,
			{
				headers: {
					Authorization: "Bearer " + accessToken,
				},
			}
		);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
