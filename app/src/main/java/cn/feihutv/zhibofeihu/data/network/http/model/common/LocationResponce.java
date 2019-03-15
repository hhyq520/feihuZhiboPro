package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by huanghao on 2017/10/13.
 */

public class LocationResponce {

    @Expose
    @SerializedName("results")
    private List<LocationEntity> locationEntities;

    public List<LocationEntity> getLocationEntities() {
        return locationEntities;
    }

    public void setLocationEntities(List<LocationEntity> locationEntities) {
        this.locationEntities = locationEntities;
    }

    public static class LocationEntity {

        /**
         * address_components : [{"long_name":"深圳市","short_name":"深圳市","types":["locality","political"]},{"long_name":"广东省","short_name":"广东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}]
         * formatted_address : 中国广东省深圳市
         * geometry : {"bounds":{"northeast":{"lat":22.8617484,"lng":114.6284667},"southwest":{"lat":22.438581,"lng":113.7514535}},"location":{"lat":22.543096,"lng":114.057865},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":22.7809313,"lng":114.3553162},"southwest":{"lat":22.3293893,"lng":113.7524414}}}
         * place_id : ChIJkVLh0Aj0AzQRyYCStw1V7v0
         * types : ["locality","political"]
         */

        private String formatted_address;
        private String place_id;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }


        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }


        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }


        @Override
        public String toString() {
            return "LocationEntity{" +
                    "formatted_address='" + formatted_address + '\'' +
                    ", place_id='" + place_id + '\'' +
                    ", types=" + types +
                    '}';
        }
    }
}
