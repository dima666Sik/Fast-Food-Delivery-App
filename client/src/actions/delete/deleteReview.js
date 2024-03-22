import axios from "axios";

export const deleteReview = async (accessToken, product_id, review_id) => {
	try {
		console.log(accessToken, product_id, review_id);
		const response = axios.delete(
			`${process.env.REACT_APP_SERVER_API_URL}api/v1/private/food-reviews/delete-review-to-product?product_id=${product_id}&review_id=${review_id}`,
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
