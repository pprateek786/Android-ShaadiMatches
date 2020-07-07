package com.shaadi.shaadimatches.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shaadi.shaadimatches.R;
import com.shaadi.shaadimatches.database.ProfileDatabase;
import com.shaadi.shaadimatches.database.model.Profile;
import com.shaadi.shaadimatches.interfaces.OnProfileButtonClick;
import com.shaadi.shaadimatches.models.Result;
import com.shaadi.shaadimatches.utils.CircleImageView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Recycler view Adapter
 */

public class ShowListAdapter extends RecyclerView.Adapter<ShowListAdapter.ProfileHolder>
{
    private List<Result> _resultList;
    private WeakReference<OnProfileButtonClick> _onProfileButtonClickWeakReference;
    public ShowListAdapter(List<Result> results, OnProfileButtonClick onProfileButtonClick)
    {
        _resultList = results;
        _onProfileButtonClickWeakReference = new WeakReference<>(onProfileButtonClick);
    }

    @NonNull
    @Override
    public ShowListAdapter.ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);
        return new ProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowListAdapter.ProfileHolder holder, int position)
    {
        holder.setDetails(_resultList.get(position), position);
    }

    @Override
    public int getItemCount()
    {
        if(_resultList == null)
        {
            return 0;
        }
        return _resultList.size();
    }


    class ProfileHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView _circleImageView;
        private AppCompatTextView _userName,_userAge;
        private AppCompatTextView _userDetails;
        private Button _acceptButton,_declineButton;
        private AppCompatTextView _txtMessage;

        public ProfileHolder(@NonNull View itemView)
        {
            super(itemView);
            _circleImageView = itemView.findViewById(R.id.user_icon);
            _userName = itemView.findViewById(R.id.userName);
            _userAge = itemView.findViewById(R.id.userAge);
            _userDetails = itemView.findViewById(R.id.user_details);
            _acceptButton = itemView.findViewById(R.id.accept);
            _declineButton = itemView.findViewById(R.id.decline);
            _txtMessage = itemView.findViewById(R.id.textMessage);
        }

        public void setDetails(Result details,int position)
        {
            Glide.with(itemView.getContext()).load(details.getPicture().getLarge()).into(_circleImageView);
            _userName.setText((String.format("%s %s %s" ,details.getName().getTitle(),details.getName().getFirst(),details.getName().getLast())));
            _userAge.setText((String.format("%s %s %S",details.getDob().getAge(),",",details.getGender())));
            _userDetails.setText(details.getLocation().getCity()+" ,"+details.getLocation().getCountry());
            _acceptButton.setTag(position);
            _declineButton.setTag(position);
            _acceptButton.setOnClickListener(this);
            _declineButton.setOnClickListener(this);

            Profile profile = ProfileDatabase.getInstance(itemView.getContext()).getProfileDao().getProfile(details.getLogin().getUsername());

            if(profile != null && profile.isUpdatedStatus())
            {
                _acceptButton.setVisibility(View.GONE);
                _declineButton.setVisibility(View.GONE);
                _txtMessage.setVisibility(View.VISIBLE);
                _txtMessage.setText("Accepted");
            }
            else
            {
                _acceptButton.setVisibility(View.VISIBLE);
                _declineButton.setVisibility(View.VISIBLE);
                _txtMessage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.accept:
                    if(_onProfileButtonClickWeakReference != null && _onProfileButtonClickWeakReference.get() !=null)
                    {
                        int positionClicked = (int) v.getTag();
                        _onProfileButtonClickWeakReference.get().onAcceptButtonClicked(positionClicked);
                    }
                    break;
                case R.id.decline:
                    if(_onProfileButtonClickWeakReference != null && _onProfileButtonClickWeakReference.get() !=null)
                    {
                        int positionClicked = (int) v.getTag();
                        _onProfileButtonClickWeakReference.get().onDeclineButtonClicked(positionClicked);
                    }
                    break;
            }

        }
    }
}
