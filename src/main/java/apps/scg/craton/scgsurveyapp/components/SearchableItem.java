package apps.scg.craton.scgsurveyapp.components;

import java.io.Serializable;

/**
 * Created by CodeThunder on 2017/03/29.
 */

public interface SearchableItem<T> extends Serializable{

    void onSearchableItemClicked(T item, int position);
}
