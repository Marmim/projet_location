package com.example.app_location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    private List<Conversation> conversations;
    private OnConversationClickListener listener;

    public ConversationsAdapter(List<Conversation> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    public void updateConversations(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewConversationName;

        ConversationViewHolder(View itemView, OnConversationClickListener listener) {
            super(itemView);
            textViewConversationName = itemView.findViewById(R.id.text_view_conversation_name);
            itemView.setOnClickListener(v -> listener.onConversationClick(conversations.get(getAdapterPosition())));
        }


        void bind(Conversation conversation) {
            textViewConversationName.setText(conversation.getName());
        }
    }

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
    }
}
