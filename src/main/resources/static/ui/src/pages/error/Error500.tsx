import ErrorPage from "../../components/util/ErrorPage";

const Error500 = () => {
    return (
        <ErrorPage code={[5, 0, 0]} title={"Not found"} message={"Something went wrong :("}/>
    )
}
export default Error500;