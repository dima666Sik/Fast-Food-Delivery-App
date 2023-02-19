import React from "react";
import AboutUs from "../components/about/AboutUs";
import Helmet from "../components/helmet/Helmet";
import Slider from "../components/slider/Slider";
import Category from "../components/ui/category/Category";

const Home = () => {
	return (
		<>
			<Helmet title="Home">
				<Slider />
				<Category />
				<AboutUs />
			</Helmet>
		</>
	);
};

export default Home;
