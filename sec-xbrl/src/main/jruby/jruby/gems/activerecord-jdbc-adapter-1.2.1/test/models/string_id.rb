class CreateStringIds < ActiveRecord::Migration
  def self.up
    create_table "string_ids", :force => true, :id => false do |t|
      t.string :id
    end
  end

  def self.down
    drop_table "string_ids"
  end
end

class StringId < ActiveRecord::Base
  def self.table_name () "string_ids" end
  # Fake out a table without a primary key
  self.primary_key = "id"
end
