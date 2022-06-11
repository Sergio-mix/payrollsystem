import {Fragment} from "react"
import {MDBDataTableV5} from "mdbreact";

const InfoFile = () => {
    return (
        <Fragment>
            <div className={""}>

            </div>
            <div className={"form-main form-table box-shadow-main border-radius-main effect-main"}>
                <div className={"d-flex"}>
                <span>TIPO DE DOCUMENTO</span>
                <span>NUMERO</span>
                <span>RAZON SOCIAL</span>
                <span>REFERENCIA</span>
                <span>SOLICITUD</span>
                </div>

            </div>
            <div className="row">
                <div className={"form-main form-first box-shadow-main border-radius-main effect-main"}>
                    <MDBDataTableV5
                        // @ts-ignore

                        entriesOptions={[5, 10]}
                        entries={5}
                        searchTop
                        searchBottom={false}
                        responsive
                        hover
                    />
                </div>
                <div className={"form-main form-aux box-shadow-main border-radius-main effect-main"}>

                </div>
            </div>
        </Fragment>
    )
}

export default InfoFile;