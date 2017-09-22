package apps.scg.craton.scgsurveyapp.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.Serializable;
import java.util.List;

import apps.scg.craton.scgsurveyapp.R;

/**
 * Created by CodeThunder on 2017/03/29.
 */

public class SearchableDialog extends DialogFragment implements SearchView.OnQueryTextListener,SearchView.OnCloseListener {

    private static final String ITEMS = "items";
    private ArrayAdapter mListAdapter;
    private ListView mListViewItems;
    private SearchableItem mSearchableItem;
    private OnSearchTextChanged mOnSearchTextChanged;
    private SearchView mSearchView;
    private String mStrTitle;
    private String mStrPositiveButtonText;
    private DialogInterface.OnClickListener onClickListener;
    private Button btnDialog;


    public SearchableDialog(){}

    public static SearchableDialog newInstance(List items)
    {
        SearchableDialog searchableDialog = new SearchableDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);

        searchableDialog.setArguments(args);

        return searchableDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE,R.style.SearchableDialog);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Getting the layout inflater to inflate the view in an alert dialog.
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            mSearchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        // Change End

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", mSearchableItem);
        super.onSaveInstanceState(outState);
    }
    // Change End

    public void setTitle(String strTitle) {
        mStrTitle = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        mStrPositiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        mStrPositiveButtonText = strPositiveButtonText;
        onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this.mSearchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.mOnSearchTextChanged = onSearchTextChanged;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        btnDialog = (Button) rootView.findViewById(R.id.btn_dialog);

        btnDialog.setOnClickListener(f -> getDialog().dismiss());
        mSearchView = (SearchView) rootView.findViewById(R.id.search);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);


        List items = (List) getArguments().getSerializable(ITEMS);

        mListViewItems = (ListView) rootView.findViewById(R.id.listItems);

        //create the adapter by passing your ArrayList data
        mListAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,items);
        //attach the adapter to the list
        mListViewItems.setAdapter(mListAdapter);

        mListViewItems.setTextFilterEnabled(true);

        mListViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchableItem.onSearchableItemClicked(mListAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
    }

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        if (TextUtils.isEmpty(s)) {
//                _listViewItems.clearTextFilter();
            ((ArrayAdapter) mListViewItems.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) mListViewItems.getAdapter()).getFilter().filter(s);
        }
        if (null != mOnSearchTextChanged) {
            mOnSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }
}
