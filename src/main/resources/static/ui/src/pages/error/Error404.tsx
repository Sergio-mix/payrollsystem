import ErrorPage from "../../components/util/ErrorPage";

const Error401 = () => {
    return (
        <ErrorPage code={[4, 0, 4]} title={"Not found"} message={"This page is currently not working"}/>
    )
}
export default Error401;