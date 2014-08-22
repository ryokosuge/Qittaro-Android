
package xyz.ryochin.qittaro.requests;

import java.io.Serializable;

public abstract class APIRequest implements Request, Serializable {

    private static final long serialVersionUID = 4590858601457765571L;
    protected int page;
    protected int perPage;

    public void setPage(int page) {
        this.page = page;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

}
