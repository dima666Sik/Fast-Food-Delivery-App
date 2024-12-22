import axios from "axios";

export const getAllReviewsToProduct = async (product_id) => {
	try {
		const response = await axios.get(
			`${process.env.REACT_APP_SERVER_API_URL}api/v2/product/foods/get-all-reviews-to-product?product_id=${product_id}`
		);
		return response;
	} catch (error) {
		console.log(error.response.data.message);
		throw error;
	}
};
