package com.grabbddemoapp.fragment

import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.model.LatLng

import com.grabbddemoapp.R
import com.grabbddemoapp.adapter.RestaurantAdapter
import com.grabbddemoapp.adapter.TypeListAdapter
import com.grabbddemoapp.data.db.CommonData
import com.grabbddemoapp.data.model.explore.ExploreResponse
import com.grabbddemoapp.data.model.explore.Item_

import com.grabbddemoapp.data.network.ApiError
import com.grabbddemoapp.data.network.CommonParams
import com.grabbddemoapp.data.network.ResponseResolver
import com.grabbddemoapp.data.network.RestClient
import com.grabbddemoapp.util.ProgressDialog
import com.grabbddemoapp.util.Utils
import java.lang.Exception
import java.util.*

class ExploreFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var currentLocation: Location
    private lateinit var tvHeaderName: TextView
    private lateinit var tvNoResult: TextView
    private lateinit var tvStart: TextView
    private lateinit var mlocationAndAddress: LocationAndAddress
    private var latitude = 0.0
    private var longitude = 0.0
    private var mRestaurantAdapter: RestaurantAdapter? = null
    private var mTypeListAdapter: TypeListAdapter? = null
    private lateinit var rvRestaurant: RecyclerView
    private lateinit var rvSection: RecyclerView
    private var mItemList: MutableList<Item_> = ArrayList()
    private var onScrollChangeListener: RecyclerView.OnScrollListener? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isLastPage = false
    private var isLoading = false

    private val LIMIT = 10
    private var SKIP = 0
    private var section: String = "food"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_explore, container, false) as ViewGroup
        init(rootView)
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            mContext = it
            mlocationAndAddress = it as LocationAndAddress
        }
    }

    fun init(view: View) {

        rvRestaurant = view.findViewById(R.id.rvRestaurant)
        tvHeaderName = view.findViewById(R.id.tvHeaderName)
        tvNoResult = view.findViewById(R.id.tvNoResult)
        tvStart = view.findViewById(R.id.tvStart)

        linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        rvRestaurant.layoutManager = linearLayoutManager

        rvSection = view.findViewById(R.id.rvSection)
        rvSection.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mTypeListAdapter = TypeListAdapter(TypeListAdapter.TypeListListener {
            section = it
            mRestaurantAdapter = null
            mItemList.clear()
            SKIP = 0
            getRestaurantList()

        })
        rvSection.adapter = mTypeListAdapter

        setOnScrollListener()
        rvRestaurant.setOnScrollListener(onScrollChangeListener)

        tvHeaderName.setOnClickListener {
            try {
                currentLocation = mlocationAndAddress.getCurrentLocation()
                Utils.searchPlace(mContext as Activity, PlaceAutocomplete.MODE_OVERLAY, LatLng(currentLocation.latitude,
                        currentLocation.longitude))
            } catch (ignored: NullPointerException) {
            }
        }

        view.findViewById<ImageView>(R.id.ivCurrentLocation).setOnClickListener {
            currentLocation = mlocationAndAddress.getCurrentLocation()
            latitude = currentLocation.latitude
            longitude = currentLocation.longitude
            mRestaurantAdapter = null
            mItemList.clear()
            SKIP = 0
            getRestaurantList()
        }
    }

    interface LocationAndAddress {
        fun getCurrentLocation(): Location
    }

    fun setAddress(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        mItemList.clear()
        mRestaurantAdapter = null
        SKIP = 0
        getRestaurantList()
    }

    private fun setOnScrollListener() {
        onScrollChangeListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= LIMIT) {
                        getRestaurantList()
                    }
                }
            }
        }
    }

    private fun getRestaurantList() {
        isLoading = true
        tvStart.visibility = View.GONE
        if (latitude == 0.0 && longitude == 0.0) {
            currentLocation = mlocationAndAddress.getCurrentLocation()
            latitude = currentLocation.latitude
            longitude = currentLocation.longitude
        }
        ProgressDialog.showProgressDialog(this@ExploreFragment.mContext)

        val v = StringBuilder()
        v.append(Calendar.getInstance().get(Calendar.YEAR))
        if (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10) {
            v.append("0")
        }
        v.append(Calendar.getInstance().get(Calendar.MONTH) + 1)
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            v.append("0")
        }
        v.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        val builder = CommonParams.Builder()
        builder.add("ll", latitude.toString() + "," + longitude.toString())
                .add("section", section)
                .add("venuePhotos", 1)
                .add("limit", LIMIT)
                .add("offset", SKIP)
                .add("v", v)
                .add("client_id", CommonData.getClientId())
                .add("client_secret", CommonData.getClientSecret())

        RestClient.getApiInterface().explore(builder.build().map).enqueue(object : ResponseResolver<ExploreResponse>() {
            override fun onSuccess(exploreResponse: ExploreResponse) {
                isLoading = false
                ProgressDialog.dismissProgressDialog()
                tvNoResult.visibility = View.GONE
                try {
                    mItemList.addAll(exploreResponse.response.groups[0].items)
                    if (mRestaurantAdapter == null) {
                        if (exploreResponse.response.groups[0].items.isEmpty()) {
                            tvNoResult.visibility = View.VISIBLE
                            tvNoResult.text = exploreResponse.response.warning.text
                        }
                        mRestaurantAdapter = RestaurantAdapter(mItemList)
                        rvRestaurant.adapter = mRestaurantAdapter

                    } else {
                        mRestaurantAdapter?.notifyItemRangeChanged(mItemList.size - 1, exploreResponse.response.groups[0].items.size)
                    }
                    SKIP += LIMIT
                    if (exploreResponse.response.groups[0].items.size < LIMIT) {
                        isLastPage = true
                    }
                } catch (ignored: Exception) {
                }
            }

            override fun onError(error: ApiError) {
                isLoading = false
                ProgressDialog.dismissProgressDialog()

            }

            override fun onFailure(throwable: Throwable) {
                isLoading = false
                ProgressDialog.dismissProgressDialog()

            }
        })

    }

}
